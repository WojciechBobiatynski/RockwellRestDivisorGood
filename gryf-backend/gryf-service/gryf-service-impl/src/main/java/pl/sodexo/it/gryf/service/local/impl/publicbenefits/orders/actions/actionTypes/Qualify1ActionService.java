package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.actionTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.MailAttachmentDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementComplexTypeEmailDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.utils.JsonMapperUtils;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.service.api.ApplicationParametersService;
import pl.sodexo.it.gryf.service.api.publicbenefits.grantapplications.GrantApplicationsService;
import pl.sodexo.it.gryf.service.local.api.MailService;
import pl.sodexo.it.gryf.service.local.api.ValidateService;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.ActionBaseService;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.elementTypes.OrderElementAttrDService;

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
    private MailService mailService;

    @Autowired
    private ValidateService validateService;

    @Autowired
    private ApplicationParametersService applicationParametersService;

    @Autowired
    private OrderElementAttrDService orderElementAttrDService;

    @Autowired
    private GrantApplicationsService grantApplicationsService;

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
        validateMail(violations, toWorkOfficeDTO, "GOQLYEMA1", "do Przedsiębiorstwa");
        validateDocuments(violations, order, "EQCONTRCT1");
        validateService.validate(violations);

        //SET DATE
        application.setConsiderationDate(new Date());
        OrderElement considerationDateElement = order.loadElement("EQQLYDATE1");
        orderElementAttrDService.updateValue(considerationDateElement, application.getConsiderationDate());

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
        if(StringUtils.isEmpty(dto.getAddressesTo())){
            violations.add(new EntityConstraintViolation(emailFieldName, String.format("Adresaci e-maila %s nie moga być puści", labelName), dto.getAddressesTo()));
        }
        if(StringUtils.isEmpty(dto.getSubject())){
            violations.add(new EntityConstraintViolation(emailFieldName, String.format("Temat e-maila %s nie może być pusty", labelName), dto.getSubject()));
        }
        if(StringUtils.isEmpty(dto.getBody())){
            violations.add(new EntityConstraintViolation(emailFieldName, String.format("Treść e-maila %s nie może być pusty", labelName), dto.getBody()));
        }
    }

    private void validateDocuments(List<EntityConstraintViolation> violations, Order order, String name){
        OrderElement orderElement = order.getElement(name);
        if(StringUtils.isEmpty(orderElement.getValueVarchar())){
            violations.add(new EntityConstraintViolation(name, "Nie wygenerowano dokumentu umowy o dofinansowanie"));
        }
    }

    private void sendMail(Order order, OrderElementComplexTypeEmailDTO dto, List<MailAttachmentDTO> attachments){
        grantApplicationsService.sendPublicGrantProgramEmail(order.getApplication(), dto.getEmailTemplateId(),
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
            if(!StringUtils.isEmpty(attachmentElement.getValueVarchar())) {
                MailAttachmentDTO attachmentDto = new MailAttachmentDTO();
                attachmentDto.setName(String.format("%s.%s", StringUtils.convertFileName(attachmentElement.getOrderFlowElement().getElementName()),
                                                                StringUtils.findFileExtension(attachmentElement.getValueVarchar())));
                attachmentDto.setPath(attachmentElement.getValueVarchar());

                attachments.add(attachmentDto);
            }
        }
        return attachments;
    }

}
