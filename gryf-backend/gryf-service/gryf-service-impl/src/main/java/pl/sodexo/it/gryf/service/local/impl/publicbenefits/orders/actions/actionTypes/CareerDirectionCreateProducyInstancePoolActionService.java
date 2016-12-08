package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.actionTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.dao.api.crud.repository.other.GryfPLSQLRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderInvoiceRepository;
import pl.sodexo.it.gryf.model.api.FinanceNoteResult;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderInvoice;
import pl.sodexo.it.gryf.service.api.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolService;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.ActionBaseService;

import java.util.List;

/**
 * Created by Isolution on 2016-10-27.
 */
@Service
public class CareerDirectionCreateProducyInstancePoolActionService extends ActionBaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CareerDirectionCreateProducyInstancePoolActionService.class);

    @Autowired
    private PbeProductInstancePoolService productInstancePoolService;

    @Autowired
    private GryfPLSQLRepository gryfPLSQLRepository;

    @Autowired
    private OrderInvoiceRepository orderInvoiceRepository;

    //PUBLIC METHODS

    public void execute(Order order, List<String> acceptedPathViolations) {
        LOGGER.debug("Uutworzenie puli bonów dla zamówienia [{}]", order.getId());
        productInstancePoolService.createProductInstancePool(order.getId());
        saveOrderInvoice(order);
    }

    private void saveOrderInvoice(Order order){
        gryfPLSQLRepository.flush();
        FinanceNoteResult financeNoteResult = gryfPLSQLRepository.createCreditNoteForOrder(order.getId());

        OrderInvoice orderInvoice = new OrderInvoice();
        orderInvoice.setOrder(order);
        orderInvoice.setInvoiceId(financeNoteResult.getInvoiceId());
        orderInvoice.setInvoiceNumber(financeNoteResult.getInvoiceNumber());
        orderInvoice.setInvoiceDate(financeNoteResult.getInvoiceDate());
        orderInvoiceRepository.save(orderInvoice);
    }

}
