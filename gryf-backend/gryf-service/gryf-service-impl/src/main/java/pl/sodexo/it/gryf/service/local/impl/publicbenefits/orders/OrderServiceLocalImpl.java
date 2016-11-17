package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderFlowRepository;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationVersion;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlow;
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
        OrderFlow orderFlow = findOrderFlow(grantApplicationVersion.getId());

        OrderFlowService orderFlowService = (OrderFlowService) BeanUtils.findBean(context, orderFlow.getServiceBeanName());
        Order order = orderFlowService.createOrder(grantApplication, orderFlow);
        orderFlowElementService.addElementsByOrderStatus(order);
        return order;
    }

    @Override
    public Order createOrder(Contract contract) {
        //TODO: tbilski na podstawie tabelki
        //GrantApplicationVersion grantApplicationVersion = grantApplication.getApplicationVersion();
        OrderFlow orderFlow = orderFlowRepository.get(100L);//findOrderFlow(grantApplicationVersion.getId());

        OrderFlowService orderFlowService = (OrderFlowService) BeanUtils.findBean(context, orderFlow.getServiceBeanName());
        Order order = orderFlowService.createOrder(contract, orderFlow);
        orderFlowElementService.addElementsByOrderStatus(order);
        return order;
    }

    /**
     * Metoda wyszukuje objekt OrderFlow na podstawie id wersji wniosku. Metoda sprawdza czy w bazie znajduje się dokałdnie
     * jedna instancja objetu OrderFlow dla danej wersji. Jeżeli nie bedzie znajdować sietylko jedna instancja zostanie wygenerowany wyjatek.
     *
     * @param id id wersji aplikacji
     * @return objekt typu OrderFlow
     */
    private OrderFlow findOrderFlow(Long id) {
        List<OrderFlow> orderFlows = orderFlowRepository.findByGrantApplicationVersionInDate(id, new Date());
        if (orderFlows.size() == 0) {
            throw new RuntimeException(String.format("Nie znaleziono żadnego order flow dla wersji [%s]", id));
        }
        if (orderFlows.size() > 1) {
            throw new RuntimeException(String.format("Dla danej wersji wniosku [%s] znaleziono wiecej niż jeden order flow", id));
        }
        return orderFlows.get(0);
    }
}

