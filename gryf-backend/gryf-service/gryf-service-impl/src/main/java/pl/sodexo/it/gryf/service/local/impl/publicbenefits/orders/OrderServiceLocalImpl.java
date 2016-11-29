package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.CreateOrderDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.contracts.ContractRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderRepository;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationVersion;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramParam;
import pl.sodexo.it.gryf.model.publicbenefits.orders.*;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.ParamInDateService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.OrderServiceLocal;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.OrderElementService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.orderflows.OrderFlowElementService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.orderflows.OrderFlowService;
import pl.sodexo.it.gryf.service.utils.BeanUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by jbentyn on 2016-09-27.
 */
@Service
@Transactional
public class OrderServiceLocalImpl implements OrderServiceLocal {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private OrderFlowElementService orderFlowElementService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ParamInDateService paramInDateService;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private GryfValidator gryfValidator;

    //PUBLIC METHODS

    @Override
    public CreateOrderDTO createCreateOrderDTO(Contract contract){
        GrantProgram grantProgram = contract.getGrantProgram();
        OrderFlowForGrantProgram orderForGrPr = paramInDateService.findOrderFlowForGrantProgram(grantProgram.getId(), new Date(), true);
        OrderFlow orderFlow = orderForGrPr.getOrderFlow();

        OrderFlowService orderFlowService = (OrderFlowService) BeanUtils.findBean(context, orderFlow.getServiceBeanName());
        return orderFlowService.createCreateOrderDTO(contract);
    }

    @Override
    public Order createOrder(CreateOrderDTO dto) {
        gryfValidator.validate(dto);

        Contract contract = contractRepository.get(dto.getContractId());
        GrantProgram grantProgram = contract.getGrantProgram();
        OrderFlowForGrantProgram orderForGrPr = paramInDateService.findOrderFlowForGrantProgram(grantProgram.getId(), new Date(), true);
        OrderFlow orderFlow = orderForGrPr.getOrderFlow();

        OrderFlowService orderFlowService = (OrderFlowService) BeanUtils.findBean(context, orderFlow.getServiceBeanName());
        Order order = orderFlowService.createOrder(contract, orderFlow, dto);
        orderFlowElementService.addElementsByOrderStatus(order);
        return order;
    }

    @Override
    public Order createOrder(GrantApplication grantApplication) {
        GrantApplicationVersion grantApplicationVersion = grantApplication.getApplicationVersion();
        OrderFlowForGrantApplicationVersion orderFlowForGraAppVer = paramInDateService.findOrderFlowForGrantApplicationVersion(grantApplicationVersion.getId(), new Date(), true);
        OrderFlow orderFlow = orderFlowForGraAppVer.getOrderFlow();

        OrderFlowService orderFlowService = (OrderFlowService) BeanUtils.findBean(context, orderFlow.getServiceBeanName());
        Order order = orderFlowService.createOrder(grantApplication, orderFlow);
        orderFlowElementService.addElementsByOrderStatus(order);
        return order;
    }

    @Override
    public List<OrderElementDTO> createOrderElementDtolist(List<OrderElementDTOBuilder> orderElementDTOBuilders) {
        List<OrderElementDTO> elements = new ArrayList<>();
        for (OrderElementDTOBuilder builder : orderElementDTOBuilders) {
            OrderFlowElement ofe = builder.getOrderFlowElement();
            OrderFlowElementType ofet = ofe.getOrderFlowElementType();

            OrderElementService service = (OrderElementService) BeanUtils.findBean(context, ofet.getServiceBeanName());
            OrderElementDTO elementDTO = service.createElement(builder);
            if (elementDTO != null) {
                elements.add(elementDTO);
            }
        }
        return elements;
    }

    @Override
    public BigDecimal getPaymentAmount(Order order) {
        if (order == null)
            return null;
        try {
            return order.getProduct().getPbeTotalValue().subtract(order.getProduct().getPbeAidValue()).multiply(new BigDecimal(order.getVouchersNumber()));
        } catch (Exception e) {
            throw new RuntimeException("Nie udało się wyznaczyć kwoty płatności dla przedsiębiorstwa, prawdopodobnie brakujące dane na zamówieniu/produkcie lub niewłaściwa parametryzacja.");
        }
    }

}

