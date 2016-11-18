package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderFlowRepository;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationVersion;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlow;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowForGrantApplicationVersion;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowForGrantProgram;
import pl.sodexo.it.gryf.service.local.api.ParamInDateService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.OrderServiceLocal;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.orderflows.OrderFlowElementService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.orderflows.OrderFlowService;
import pl.sodexo.it.gryf.service.utils.BeanUtils;

import java.math.BigDecimal;
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
    private OrderFlowRepository orderFlowRepository;

    @Autowired
    private ParamInDateService paramInDateService;


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

    @Override
    public Order createOrder(GrantApplication grantApplication) {
        GrantApplicationVersion grantApplicationVersion = grantApplication.getApplicationVersion();
        OrderFlowForGrantApplicationVersion orderFlowForGraAppVer = paramInDateService.findOrderFlowForGrantApplicationVersion(grantApplicationVersion.getId(), new Date());
        OrderFlow orderFlow = orderFlowForGraAppVer.getOrderFlow();

        OrderFlowService orderFlowService = (OrderFlowService) BeanUtils.findBean(context, orderFlow.getServiceBeanName());
        Order order = orderFlowService.createOrder(grantApplication, orderFlow);
        orderFlowElementService.addElementsByOrderStatus(order);
        return order;
    }

    @Override
    public Order createOrder(Contract contract) {
        GrantProgram grantProgram = contract.getGrantProgram();
        OrderFlowForGrantProgram orderForGrPr = paramInDateService.findOrderFlowForGrantProgram(grantProgram.getId(), new Date());
        OrderFlow orderFlow = orderForGrPr.getOrderFlow();

        OrderFlowService orderFlowService = (OrderFlowService) BeanUtils.findBean(context, orderFlow.getServiceBeanName());
        Order order = orderFlowService.createOrder(contract, orderFlow);
        orderFlowElementService.addElementsByOrderStatus(order);
        return order;
    }

}

