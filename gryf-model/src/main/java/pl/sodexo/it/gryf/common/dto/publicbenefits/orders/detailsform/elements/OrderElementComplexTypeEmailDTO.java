package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements;

import pl.sodexo.it.gryf.common.dto.MailDTO;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowElement;

/**
 * Created by tomasz.bilski.ext on 2015-09-18.
 */
public class OrderElementComplexTypeEmailDTO extends OrderElementDTO {

    //FIELDS

    private String addressesTo;

    private String subject;

    private String body;
    
    private String emailTemplateId;
    
    private String emailServiceBean;

    public OrderElementComplexTypeEmailDTO(){
    }

    public OrderElementComplexTypeEmailDTO(OrderElementDTOBuilder builder, MailDTO mailDTO){
        super(builder);
        this.addressesTo = mailDTO.getAddressesTo();
        this.subject = mailDTO.getSubject();
        this.body = mailDTO.getBody();
        this.emailTemplateId = mailDTO.getTemplateId();
        this.emailServiceBean = builder.getOrderFlowElement().getPropertyValue(OrderFlowElement.EMAIL_SERVICE_BEAN);
    }

    public OrderElementComplexTypeEmailDTO(OrderElementDTOBuilder builder, OrderElementComplexTypeEmailDTO dto){
        super(builder);
        this.addressesTo = dto.getAddressesTo();
        this.subject = dto.getSubject();
        this.body = dto.getBody();
        this.emailTemplateId = dto.getEmailTemplateId();
        this.emailServiceBean = builder.getOrderFlowElement().getPropertyValue(OrderFlowElement.EMAIL_SERVICE_BEAN);
    }

    //GETTERS & SETTERS

    public String getAddressesTo() {
        return addressesTo;
    }

    public void setAddressesTo(String addressesTo) {
        this.addressesTo = addressesTo;
    }



    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getEmailTemplateId() {
        return emailTemplateId;
    }

    public void setEmailTemplateId(String emailTemplateId) {
        this.emailTemplateId = emailTemplateId;
    }

    public String getEmailServiceBean() {
        return emailServiceBean;
    }

    public void setEmailServiceBean(String emailServiceBean) {
        this.emailServiceBean = emailServiceBean;
    }


    
}
