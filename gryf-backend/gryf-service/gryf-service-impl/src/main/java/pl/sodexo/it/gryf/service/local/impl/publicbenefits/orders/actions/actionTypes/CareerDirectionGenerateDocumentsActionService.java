package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.actionTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderInvoice;
import pl.sodexo.it.gryf.model.reports.ReportInstance;
import pl.sodexo.it.gryf.service.local.api.reports.ReportService;
import pl.sodexo.it.gryf.service.local.api.AccountingDocumentArchiveFileService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.elementTypes.OrderElementAttachmentService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.orderflows.OrderFlowElementService;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.ActionBaseService;

import java.util.List;

import static pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElementCons.KK_DOCUMENT_OWN_CONTRIBUTION_ELEM_ID;

/**
 * Created by Isolution on 2016-11-24.
 */
@Service
public class CareerDirectionGenerateDocumentsActionService extends ActionBaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CareerDirectionGenerateDocumentsActionService.class);

    //FIELDS

    @Autowired
    private ReportService reportService;

    @Autowired
    private AccountingDocumentArchiveFileService accountingDocumentArchiveFileService;

    @Autowired
    private OrderElementAttachmentService orderElementAttachmentService;

    @Autowired
    private OrderFlowElementService orderFlowElementService;

    //PUBLIC METHODS

    public void execute(Order order, List<String> acceptedPathViolations) {
        LOGGER.debug("Utworzenie dokumentów księgowych dla zamówienia [{}]", order.getId());

        //GENEROWANIE RAPORTU
        OrderInvoice orderInvoice = getOrderInvoice(order);
        ReportInstance reportInstance = reportService.generateDebitNoteForOrder(order.getId(), orderInvoice.getInvoiceNumber());
        accountingDocumentArchiveFileService.createAccountingDocument(orderInvoice.getInvoiceId(),
                                                    orderInvoice.getInvoiceNumber(), reportInstance.getPath(), reportInstance.getParameters());

        //DTO DLA DANEGO ELEMENT
        orderFlowElementService.addElementEmpty(order, KK_DOCUMENT_OWN_CONTRIBUTION_ELEM_ID);
        OrderElement orderElement = order.getElement(KK_DOCUMENT_OWN_CONTRIBUTION_ELEM_ID);
        orderElementAttachmentService.updateValue(orderElement, reportInstance.getPath());
    }

    //PRIVATE METHODS

    private OrderInvoice getOrderInvoice(Order order){
        List<OrderInvoice> orderInvoices = order.getOrderInvoices();
        if(orderInvoices.size() == 0){
            throw new RuntimeException(String.format("Błąd parmetryzacji. Zamówienie [%s] nie zawiera żadnej noty.", order.getId()));
        }
        if(orderInvoices.size() > 1){
            throw new RuntimeException(String.format("Błąd parmetryzacji. Zamówienie [%s] zawiera wiecej niż jedną notę.", order.getId()));
        }
        return orderInvoices.get(0);
    }
}