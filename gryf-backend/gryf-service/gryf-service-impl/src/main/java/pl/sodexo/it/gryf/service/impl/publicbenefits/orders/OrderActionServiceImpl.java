package pl.sodexo.it.gryf.service.impl.publicbenefits.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.action.IncomingOrderElementDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementDTO;
import pl.sodexo.it.gryf.common.exception.StaleDataException;
import pl.sodexo.it.gryf.common.utils.LoginUtils;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderFlowStatusTransSqlRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderFlowStatusTransitionRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderRepository;
import pl.sodexo.it.gryf.model.publicbenefits.orders.*;
import pl.sodexo.it.gryf.service.api.publicbenefits.orders.OrderActionService;
import pl.sodexo.it.gryf.service.api.publicbenefits.orders.OrderService;
import pl.sodexo.it.gryf.service.api.security.SecurityCheckerService;
import pl.sodexo.it.gryf.service.local.api.FileService;
import pl.sodexo.it.gryf.service.local.api.ValidateService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.OrderDateService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.orderflows.OrderFlowElementService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.actions.ActionService;
import pl.sodexo.it.gryf.service.utils.BeanUtils;

import java.util.List;
import java.util.Objects;

/**
 * Created by jbentyn on 2016-09-27.
 */
@Service
@Transactional
public class OrderActionServiceImpl implements OrderActionService {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private SecurityCheckerService securityCheckerService;

    @Autowired
    private ValidateService validateService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private OrderFlowStatusTransSqlRepository orderFlowStatusTransSqlRepository;

    @Autowired
    private OrderFlowStatusTransitionRepository orderFlowStatusTransitionRepository;

    @Autowired
    private OrderFlowElementService orderFlowElementService;

    @Autowired
    private OrderDateService orderDateService;

    @Override
    public void executeAction(Long id, Long actionId, Integer version, List<IncomingOrderElementDTO> incomingOrderElements, List<FileDTO> files, List<String> acceptedViolations) {

        try {

            //ORDER STUFF
            Order order = orderRepository.get(id);
            OrderFlow orderFlow = order.getOrderFlow();
            OrderFlowStatus status = order.getStatus();

            //VALIDATE VERSION
            validateVersion(order, version);

            //STATUS TRANSITION
            OrderFlowStatusTransitionPK statusTransitionId = new OrderFlowStatusTransitionPK(orderFlow.getId(), status.getStatusId(), actionId);
            OrderFlowStatusTransition statusTransition = orderFlowStatusTransitionRepository.get(statusTransitionId);

            //UPRAWNINIE DO AKCJI
            validateActionPrivilege(statusTransition);

            //CREATE ORDER ELEMENT DTO
            List<OrderElementDTO> elementDtoList = orderFlowElementService.createElementDtoList(incomingOrderElements, files);

            //VALIDACJA ELEMENTOW
            orderFlowElementService.validateElements(order, elementDtoList);

            //UPDATE ELEMENTS
            orderFlowElementService.updateElements(order, elementDtoList);

            //EXECUTE PRE SQL
            executeSql(order, statusTransition, OrderFlowStatusTransSqlType.PRE);

            //EXECUTE ACTION
            if (!StringUtils.isEmpty(statusTransition.getActionBeanName())) {
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

            //KASUJEMY PLIKI
        } catch (RuntimeException e) {
            OrderService orderService = context.getBean(OrderServiceImpl.class);
            fileService.deleteSavedFiles(files);
            throw e;
        }
    }

    private void validateVersion(Order order, Integer version) {
        if (!Objects.equals(order.getVersion(), version)) {
            throw new StaleDataException(order.getId(), order);
        }
    }

    /**
     * Sprawdza uprawnine do wykoninia akcji.
     *
     * @param statusTransition obiekt reprezentujacy przejscie pomiedzy akcjiami
     */
    private void validateActionPrivilege(OrderFlowStatusTransition statusTransition) {
        String privilege = statusTransition.getAugIdRequired();
        if (!StringUtils.isEmpty(privilege)) {
            if (!securityCheckerService.hasPrivilege(privilege)) {
                validateService.validate("Nie posiadasz uprawnień do wykonania danej akcji");
            }
        }
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
            if (type == transSql.getType()) {
                orderFlowStatusTransSqlRepository.flush();
                orderFlowStatusTransSqlRepository.executeNativeSql(transSql, order.getId(), LoginUtils.getLogin());
            }
        }
    }
}
