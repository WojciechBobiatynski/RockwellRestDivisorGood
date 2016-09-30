package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements;

import lombok.ToString;

/**
 * Created by tomasz.bilski.ext on 2015-09-18.
 */
@ToString
public class OrderElementComplexTypeEmailDTO extends OrderElementDTO {

    //FIELDS

    private String addressesTo;

    private String subject;

    private String body;
    
    private String emailTemplateId;
    
    private String emailServiceBean;

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
