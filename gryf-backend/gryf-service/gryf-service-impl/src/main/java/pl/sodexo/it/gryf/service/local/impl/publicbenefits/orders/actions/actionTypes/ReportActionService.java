package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.actionTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.enums.FileType;
import pl.sodexo.it.gryf.common.enums.ReportTemplateCode;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.service.api.other.ApplicationParameters;
import pl.sodexo.it.gryf.service.api.reports.ReportService;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.ActionBaseService;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.elementTypes.OrderElementAttachmentService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tomasz.bilski.ext on 2015-09-02.
 */
@Service
public class ReportActionService extends ActionBaseService {

    //FIELDS

    @Autowired
    private ReportService reportService;

    @Autowired
    private OrderElementAttachmentService orderElementAttachmentService;

    @Autowired
    private ApplicationParameters applicationParameters;

    //PUBLIC METHODS

    @Override
    protected void executeAction(Order order){

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("invoiceId", 1308L);//TODO; PRZEKAZAC
        parameters.put("companyName", applicationParameters.getSodexoName());
        parameters.put("companyAddress1", applicationParameters.getSodexoAddress1());
        parameters.put("companyAddress2", applicationParameters.getSodexoAddress2());
        parameters.put("companyVatRegNum", applicationParameters.getSodexoVatRegNum());


        String reportFileName = String.format("Invoice_%s.pdf", order.getId());
        String reportLocation = reportService.generateReport(ReportTemplateCode.INVOICE, reportFileName,
                                                                FileType.ORDERS, parameters);

        //DTO DLA DANEGO ELEMENT
        OrderElement orderElement = order.getElement("ATT_REP_T");
        if(orderElement != null) {
            orderElementAttachmentService.updateValue(orderElement, reportLocation);
        }
    }
}
