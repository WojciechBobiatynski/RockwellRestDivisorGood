package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.actionTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.MailAttachmentDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementComplexTypeEmailDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.common.utils.JsonMapperUtils;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.grantapplications.GrantApplicationEmailService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.elementTypes.OrderElementAttrDService;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.ActionBaseService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-22.
 */
@Service
public class Qualify1ActionService extends ActionBaseService {

    //FIELDS

    @Autowired
    private GryfValidator gryfValidator;

    @Autowired
    private OrderElementAttrDService orderElementAttrDServiceI;

    @Autowired
    private GrantApplicationEmailService grantApplicationEmailService;

    //PUBLIC METHODS

    @Override
    protected void executeAction(Order order){
        GrantApplication application = order.getApplication();

        //MAIL - TO ENTERPRISE
        OrderElementComplexTypeEmailDTO toEnterpriseDTO = getDto(order, "ENTQLYEMA1");
        List<MailAttachmentDTO> toEnterpriseAttachments = getAttachments(order, "EQEATT1_");

        //MAIL - WORK OFFICE
        OrderElementComplexTypeEmailDTO toWorkOfficeDTO = getDto(order, "GOQLYEMA1");
        List<MailAttachmentDTO> toWorkOfficeAttachments = getAttachments(order, "GOQEATT1_");

        //VALIDATE
        List<EntityConstraintViolation> violations = new ArrayList<>();
        validateMail(violations, toEnterpriseDTO, "ENTQLYEMA1", "do WUP");
        validateMail(violations, toWorkOfficeDTO, "GOQLYEMA1", "do Przedsi??biorstwa");
        validateDocuments(violations, order, "EQCONTRCT1");
        gryfValidator.validate(violations);

        //SET DATE
        application.setConsiderationDate(new Date());
        OrderElement considerationDateElement = order.loadElement("EQQLYDATE1");
        orderElementAttrDServiceI.updateValue(considerationDateElement, application.getConsiderationDate());

        //SEND MAIL
        sendMail(order, toEnterpriseDTO, toEnterpriseAttachments);
        sendMail(order, toWorkOfficeDTO, toWorkOfficeAttachments);
    }

    //PRIVATE METHODS

    private List<OrderElement> getAttachmentElements(Order order, String prefix){
        List<OrderElement> result = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            OrderElement element = order.getElement(prefix + i);
            if(element != null){
                result.add(element);
            }
        }
        return result;
    }

    private void validateMail(List<EntityConstraintViolation> violations, OrderElementComplexTypeEmailDTO dto, String emailFieldName, String labelName){
        if(GryfStringUtils.isEmpty(dto.getAddressesTo())){
            violations.add(new EntityConstraintViolation(emailFieldName, String.format("Adresaci e-maila %s nie moga by?? pu??ci", labelName), dto.getAddressesTo()));
        }
        if(GryfStringUtils.isEmpty(dto.getSubject())){
            violations.add(new EntityConstraintViolation(emailFieldName, String.format("Temat e-maila %s nie mo??e by?? pusty", labelName), dto.getSubject()));
        }
        if(GryfStringUtils.isEmpty(dto.getBody())){
            violations.add(new EntityConstraintViolation(emailFieldName, String.format("Tre???? e-maila %s nie mo??e by?? pusty", labelName), dto.getBody()));
        }
    }

    private void validateDocuments(List<EntityConstraintViolation> violations, Order order, String name){
        OrderElement orderElement = order.getElement(name);
        if(GryfStringUtils.isEmpty(orderElement.getValueVarchar())){
            violations.add(new EntityConstraintViolation(name, "Nie wygenerowano dokumentu umowy o dofinansowanie"));
        }
    }

    private void sendMail(Order order, OrderElementComplexTypeEmailDTO dto, List<MailAttachmentDTO> attachments){
        grantApplicationEmailService.sendPublicGrantProgramEmail(order.getApplication(), dto.getEmailTemplateId(),
                                                                dto.getSubject(), dto.getBody(), dto.getAddressesTo(),
                                                                attachments);
    }

    private OrderElementComplexTypeEmailDTO getDto(Order order, String elementName){
        OrderElement orderElement = order.getElement(elementName);
        if(orderElement != null){
            if(orderElement.getClob() != null){
                String valueClob =  orderElement.getClob().getValueClob();
                return JsonMapperUtils.readValue(valueClob, OrderElementComplexTypeEmailDTO.class);
            }
        }
        return null;
    }

    private List<MailAttachmentDTO> getAttachments(Order order, String elementPrefix){
        List<OrderElement> attachmentElements = getAttachmentElements(order, elementPrefix);

        List<MailAttachmentDTO> attachments = new ArrayList<>();
        for (OrderElement attachmentElement : attachmentElements) {
            if(!GryfStringUtils.isEmpty(attachmentElement.getValueVarchar())) {
                MailAttachmentDTO attachmentDto = new MailAttachmentDTO();
                attachmentDto.setName(String.format("%s.%s", GryfStringUtils.convertFileName(attachmentElement.getOrderFlowElement().getElementName()),
                                                                GryfStringUtils.findFileExtension(attachmentElement.getValueVarchar())));
                attachmentDto.setPath(attachmentElement.getValueVarchar());

                attachments.add(attachmentDto);
            }
        }
        return attachments;
    }

}
