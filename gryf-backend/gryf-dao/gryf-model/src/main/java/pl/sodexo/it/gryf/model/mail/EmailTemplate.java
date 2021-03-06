package pl.sodexo.it.gryf.model.mail;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.GryfEntity;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by tomasz.bilski.ext on 2015-08-20.
 */
@ToString
@Entity
@Table(name = "EMAIL_TEMPLATES", schema = "APP_PBE")
public class EmailTemplate extends GryfEntity{

    //STATIC FIELDS

    /**
     * Standardowy szablon e-maila z placeholderami {$emailBody} i {$emailSubject}
     */
    public static final String STD_EMAIL = "STD_EMAIL";
    /**
     * Szablon odrzucenia wniosku przedsiębiorstwa, placeholdery w DB
     */
    public static final String GA_REJECT = "GA_REJECT";
    /**
     * Szablon informacji o kwalifikacji do programu wysyłany do MŚP, placeholdery w DB
     */
    public static final String GA_QUALIFY = "GA_QUALIFY";

    /**
     * Szablon informacji o kwalifikacji do programu wysyłany do WUP, placeholdery w DB
     */
    public static final String GA_QLY_GO = "GA_QLY_GO";
    

    //FIELDS

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "EMAIL_BODY_TEMPLATE")
    private String emailBodyTemplate;

    @Column(name = "EMAIL_BODY_HTML_TEMPLATE")
    private String emailBodyHtmlTemplate;

    @Column(name = "EMAIL_SUBJECT_TEMPLATE")
    private String emailSubjectTemplate;

    @Enumerated(EnumType.STRING)
    @Column(name = "EMAIL_TYPE")
    private EmailType emailType;

    @Column(name = "DESCRIPTION")
    private String description;

    //GETTERS & SETTERS

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmailBodyTemplate() {
        return emailBodyTemplate;
    }

    public void setEmailBodyTemplate(String emailBodyTemplate) {
        this.emailBodyTemplate = emailBodyTemplate;
    }

    public String getEmailBodyHtmlTemplate() {
        return emailBodyHtmlTemplate;
    }

    public void setEmailBodyHtmlTemplate(String emailBodyHtmlTemplate) {
        this.emailBodyHtmlTemplate = emailBodyHtmlTemplate;
    }

    public String getEmailSubjectTemplate() {
        return emailSubjectTemplate;
    }

    public void setEmailSubjectTemplate(String emailSubjectTemplate) {
        this.emailSubjectTemplate = emailSubjectTemplate;
    }

    public EmailType getEmailType() {
        return emailType;
    }

    public void setEmailType(EmailType emailType) {
        this.emailType = emailType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //EQUALS & HASH CODE

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        return Objects.equals(id, ((EmailTemplate) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }
}
