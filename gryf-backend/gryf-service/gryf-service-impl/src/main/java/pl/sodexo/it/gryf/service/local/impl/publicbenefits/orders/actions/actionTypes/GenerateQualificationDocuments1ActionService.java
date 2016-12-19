package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.actionTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.enums.FileType;
import pl.sodexo.it.gryf.common.enums.ReportParameter;
import pl.sodexo.it.gryf.common.enums.ReportSourceType;
import pl.sodexo.it.gryf.common.enums.ReportTemplateCode;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.service.api.reports.ReportService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.elementTypes.OrderElementAttachmentService;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.ActionBaseService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tomasz.bilski.ext on 2015-09-22.
 */
@Service
public class GenerateQualificationDocuments1ActionService extends ActionBaseService {

    //FIELDS

    @Autowired
    private ReportService reportService;

    @Autowired
    private OrderElementAttachmentService orderElementAttachmentService;

    //ACTION

    @Override
    protected void executeAction(Order order){

        //GENERATOE REPORT
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(ReportParameter.ORDER_ID.getParam(), order.getId());
        Long entityId = order.getEnterprise() != null ? order.getEnterprise().getId() :
                (order.getContract() != null && order.getContract().getIndividual() != null ?
                                                        order.getContract().getIndividual().getId() : null);

        String reportFileName = String.format("%s_%s_Umowa_z_przedsiebiorstewem_o_dofinansowanie.pdf", entityId, order.getId());
        String reportLocation = reportService.generateReport(ReportTemplateCode.ENTERPRISE_AGREEMENT, reportFileName,
                FileType.ORDERS, parameters, ReportSourceType.ORDER, order.getId());

        //DTO DLA DANEGO ELEMENT
        OrderElement orderElement = order.getElement("EQCONTRCT1");
        if(orderElement != null) {
            orderElementAttachmentService.updateValue(orderElement, reportLocation);
        }
    }
}
