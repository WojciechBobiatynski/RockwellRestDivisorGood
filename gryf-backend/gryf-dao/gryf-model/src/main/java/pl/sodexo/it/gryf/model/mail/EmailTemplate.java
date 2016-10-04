package pl.sodexo.it.gryf.model.mail;

import lombok.ToString;
import pl.sodexo.it.gryf.model.GryfEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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

    @Column(name = "EMAIL_SUBJECT_TEMPLATE")
    private String emailSubjectTemplate;

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

    public String getEmailSubjectTemplate() {
        return emailSubjectTemplate;
    }

    public void setEmailSubjectTemplate(String emailSubjectTemplate) {
        this.emailSubjectTemplate = emailSubjectTemplate;
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
