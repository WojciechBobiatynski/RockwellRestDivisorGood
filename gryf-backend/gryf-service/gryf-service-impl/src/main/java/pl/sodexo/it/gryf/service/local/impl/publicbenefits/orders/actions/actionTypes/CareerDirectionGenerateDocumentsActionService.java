package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.actionTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.enums.FileType;
import pl.sodexo.it.gryf.common.enums.ReportParameter;
import pl.sodexo.it.gryf.common.enums.ReportTemplateCode;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.service.api.reports.ReportService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.elementTypes.OrderElementAttachmentService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.orderflows.OrderFlowElementService;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.ActionBaseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private OrderElementAttachmentService orderElementAttachmentService;

    @Autowired
    private OrderFlowElementService orderFlowElementService;

    @Autowired
    private ApplicationParameters applicationParameters;
    //PUBLIC METHODS

    public void execute(Order order, List<String> acceptedPathViolations) {
        LOGGER.debug("Utworzenie dokumentów księgowych dla zamówienia [{}]", order.getId());

        //GENERATOE REPORT
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(ReportParameter.ORDER_ID.getParam(), order.getId());
        parameters.put("companyName", applicationParameters.getSodexoName());
        parameters.put("companyAddress1", applicationParameters.getSodexoAddress1());
        parameters.put("companyAddress2", applicationParameters.getSodexoAddress2());
        parameters.put("companyVatRegNum", applicationParameters.getSodexoVatRegNum());
        parameters.put("companyBankName", applicationParameters.getSodexoBankName());
        Long entityId = order.getEnterprise() != null ? order.getEnterprise().getId() : order.getContract().getIndividual().getId();

        //TODO: tbilski nazwa pliku
        String reportFileName = String.format("%s_%s_Nota_obciazeniowo_ksiegowa_na_wklad_wlasny.pdf", entityId, order.getId());
        String reportLocation = reportService.generateReport(ReportTemplateCode.DEBIT_NOTE, reportFileName,
                                                            FileType.ORDERS, parameters);

        //DTO DLA DANEGO ELEMENT
        orderFlowElementService.addElementEmpty(order, KK_DOCUMENT_OWN_CONTRIBUTION_ELEM_ID);
        OrderElement orderElement = order.getElement(KK_DOCUMENT_OWN_CONTRIBUTION_ELEM_ID);
        orderElementAttachmentService.updateValue(orderElement, reportLocation);
    }
}