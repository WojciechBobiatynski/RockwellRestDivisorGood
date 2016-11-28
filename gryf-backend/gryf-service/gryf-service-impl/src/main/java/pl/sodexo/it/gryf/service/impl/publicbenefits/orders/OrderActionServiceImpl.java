package pl.sodexo.it.gryf.service.impl.publicbenefits.orders;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.action.IncomingOrderElementDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementDTO;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderElementRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderFlowStatusTransSqlRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderFlowStatusTransitionRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderRepository;
import pl.sodexo.it.gryf.model.publicbenefits.orders.*;
import pl.sodexo.it.gryf.service.api.publicbenefits.orders.OrderActionService;
import pl.sodexo.it.gryf.service.local.api.FileService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.OrderDateService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.OrderServiceLocal;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.actions.ActionService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.orderflows.OrderFlowElementService;
import pl.sodexo.it.gryf.service.utils.BeanUtils;
import pl.sodexo.it.gryf.service.validation.VersionableValidator;
import pl.sodexo.it.gryf.service.validation.publicbenefits.orders.OrderActionValidator;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by jbentyn on 2016-09-27.
 */
@Service
@Transactional
public class OrderActionServiceImpl implements OrderActionService {

    //FIELDS

    private OrderActionService orderActionService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private OrderServiceLocal orderServiceLocal;

    @Autowired
    private OrderFlowStatusTransSqlRepository orderFlowStatusTransSqlRepository;

    @Autowired
    private OrderFlowStatusTransitionRepository orderFlowStatusTransitionRepository;

    @Autowired
    private OrderElementRepository orderElementRepository;

    @Autowired
    private OrderFlowElementService orderFlowElementService;

    @Autowired
    private OrderDateService orderDateService;

    @Autowired
    private OrderActionValidator orderActionValidator;

    @Autowired
    private VersionableValidator versionableValidator;

    //LIFECYCLE METHODS

    @PostConstruct
    private void init() {
        orderActionService = context.getBean(OrderActionService.class);
    }

    //PUBLIC METHODS

    @Override
    public void executeAction(Long id, Long actionId, Integer version, List<IncomingOrderElementDTO> incomingOrderElements, List<FileDTO> files, List<String> acceptedViolations) {
        orderActionService.executeMainAction(id, actionId, version, incomingOrderElements, files, acceptedViolations);

        while(true){
            Integer automaticTransNum = orderFlowStatusTransitionRepository.countAutomaticTransitionByOrder(id);
            if(automaticTransNum == 0){
                break;
            }if (automaticTransNum > 1){
                throw new RuntimeException(String.format("Dla zamówienia [%s] istnieje wiecej niż jedno przejście automatyczne", id));
            }
            orderActionService.executeAutomaticActions(id);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void executeMainAction(Long id, Long actionId, Integer version, List<IncomingOrderElementDTO> incomingOrderElements, List<FileDTO> files, List<String> acceptedViolations) {
        try {
            //ORDER STUFF
            Order order = orderRepository.get(id);
            OrderFlow orderFlow = order.getOrderFlow();
            OrderFlowStatus status = order.getStatus();

            //VALIDATE VERSION
            versionableValidator.validateVersion(order, version, order.getId());

            //STATUS TRANSITION
            OrderFlowStatusTransitionPK statusTransitionId = new OrderFlowStatusTransitionPK(orderFlow.getId(), status.getStatusId(), actionId);
            OrderFlowStatusTransition statusTransition = orderFlowStatusTransitionRepository.get(statusTransitionId);

            //UPRAWNINIE DO AKCJI
            orderActionValidator.validateActionPrivilege(statusTransition);

            //CREATE ORDER ELEMENT DTO
            List<OrderElementDTO> elementDtoList = orderFlowElementService.createElementDtoList(incomingOrderElements, files);

            //ACTION
            executeOneAction(order, statusTransition, elementDtoList, acceptedViolations);

            //KASUJEMY PLIKI
        } catch (RuntimeException e) {
            fileService.deleteSavedFiles(files);
            throw e;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void executeAutomaticActions(Long orderId){
        Order order = orderRepository.get(orderId);
        OrderFlowStatus status = order.getStatus();
        for(OrderFlowStatusTransition st : status.getOrderFlowStatusTransitions()){
            if(st.getAutomatic()){
                List<OrderElementDTOBuilder> orderElementDTOBuilders = orderElementRepository.findDtoFactoryByOrderToModify(orderId);
                List<OrderElementDTO> elementDtoList = orderServiceLocal.createOrderElementDtolist(orderElementDTOBuilders);
                executeOneAction(order, st, elementDtoList, Lists.newArrayList());
            }
        }
    }

    //PRIVATE METHODS

    private void executeOneAction(Order order, OrderFlowStatusTransition statusTransition,
                                    List<OrderElementDTO> elementDtoList, List<String> acceptedViolations){

        //VALIDACJA ELEMENTOW
        orderFlowElementService.validateElements(order, elementDtoList);

        //UPDATE ELEMENTS
        orderFlowElementService.updateElements(order, elementDtoList);

        //EXECUTE PRE SQL
        executeSql(order, statusTransition, OrderFlowStatusTransSqlType.PRE);

        //EXECUTE ACTION
        if (!GryfStringUtils.isEmpty(statusTransition.getActionBeanName())) {
            ActionService actionService = (ActionService) BeanUtils.findBean(context, statusTransition.getActionBeanName());
            actionService.execute(order, acceptedViolations);
        }

        //EXECUTE POST SQL
        executeSql(order, statusTransition, OrderFlowStatusTransSqlType.POST);

        //SET STATUS
        order.setStatus(statusTransition.getNextStatus());
        orderFlowElementService.addElementsByOrderStatus(order);

        //FILL REQUIRED DATE FOR LAZY FIELDS
        orderDateService.fillRequiredDateForLazyFields(order);
    }

    /**
     * Wykonywany jest sql, dopiey do konkretnego przejścia między statusami.
     *
     * @param order zamówienie
     * @param statusTransition przejsćie pomiędzy statusami
     * @param type typ sql PRE lub POST
     */
    private void executeSql(Order order, OrderFlowStatusTransition statusTransition, OrderFlowStatusTransSqlType type) {
        List<OrderFlowStatusTransSql> transSqlList = statusTransition.getOrderFlowStatusTransSqlList();
        for (OrderFlowStatusTransSql transSql : transSqlList) {
            if (type != transSql.getType())continue;
            
            orderFlowStatusTransSqlRepository.flush();
            orderFlowStatusTransSqlRepository.executeNativeSql(transSql, order.getId(), GryfUser.getLoggedUserLogin());
        }
    }
}
