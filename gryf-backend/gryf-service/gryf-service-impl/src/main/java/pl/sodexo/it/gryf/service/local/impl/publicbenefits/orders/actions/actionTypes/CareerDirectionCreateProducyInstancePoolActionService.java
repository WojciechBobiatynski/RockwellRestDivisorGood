package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.actionTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.dao.api.crud.repository.other.GryfPLSQLRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderInvoiceRepository;
import pl.sodexo.it.gryf.model.api.FinanceNoteResult;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElementCons;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderInvoice;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.orderflows.OrderFlowElementService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.pbeproduct.PbeProductInstancePoolLocalService;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.ActionBaseService;

import java.util.List;

/**
 * Created by Isolution on 2016-10-27.
 */
@Service
public class CareerDirectionCreateProducyInstancePoolActionService extends ActionBaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CareerDirectionCreateProducyInstancePoolActionService.class);

    @Autowired
    private PbeProductInstancePoolLocalService productInstancePoolLocalService;

    @Autowired
    private GryfPLSQLRepository gryfPLSQLRepository;

    @Autowired
    private OrderInvoiceRepository orderInvoiceRepository;

    @Autowired
    private OrderFlowElementService orderFlowElementService;

    //PUBLIC METHODS

    public void execute(Order order, List<String> acceptedPathViolations) {
        LOGGER.debug("Uutworzenie puli bonów dla zamówienia [{}]", order.getId());
        productInstancePoolLocalService.createPool(order);
        saveOrderInvoice(order);
    }

    private void saveOrderInvoice(Order order){
        gryfPLSQLRepository.flush();
        FinanceNoteResult financeNoteResult = gryfPLSQLRepository.createDebitNoteForOrder(order.getId(), GryfUser.getLoggedUserLogin());

        orderFlowElementService.addElementCheckboxValue(order, OrderElementCons.KK_WUP_DEBT_DOCUMENT_EXPORTED_ELEM_ID, false);
        orderFlowElementService.addElementVarcharValue(order, OrderElementCons.KK_WUP_DEBT_DOCUMENT_NUMBER_ELEM_ID,
                                                                financeNoteResult.getWupDebtDocumentNumber());

        OrderInvoice orderInvoice = new OrderInvoice();
        orderInvoice.setOrder(order);
        orderInvoice.setIndividual(order.getContract() != null ? order.getContract().getIndividual() : null);
        orderInvoice.setEnterprise(order.getContract() != null ? order.getContract().getEnterprise() : null);
        orderInvoice.setInvoiceId(financeNoteResult.getInvoiceId());
        orderInvoice.setInvoiceNumber(financeNoteResult.getInvoiceNumber());
        orderInvoice.setInvoiceType(financeNoteResult.getInvoiceType());
        orderInvoice.setInvoiceDate(financeNoteResult.getInvoiceDate());
        orderInvoice.setWupDebtDocumentNumber(financeNoteResult.getWupDebtDocumentNumber());
        order.addOrderInvoice(orderInvoice);
        orderInvoiceRepository.save(orderInvoice);
    }

}
