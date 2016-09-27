package pl.sodexo.it.gryf.model.publicbenefits.enterprises;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.model.AuditableEntity;
import pl.sodexo.it.gryf.model.publicbenefits.ContactType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Created by tomasz.bilski.ext on 2015-06-24.
 */
@ToString
@Entity
@Table(name = "ENTERPRISE_CONTACTS", schema = "APP_PBE")
public class EnterpriseContact  extends AuditableEntity {

    //STATIC FIELDS - ATRIBUTES

    public static final String CONTACT_DATA_ATTR_NAME = "contactData";
    public static final String ENTERPRISE_ATTR_NAME = "enterprise";

    //PRIVATE FIELDS

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "pk_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CONTACT_TYPE")
    @NotNull(message = "Typ kontaktu nie może być pusty")
    private ContactType contactType;

    @Column(name = "CONTACT_DATA")
    @NotEmpty(message = "Dane kontaktowe nie mogą być puste")
    private String contactData;

    @Column(name = "REMARKS")
    private String remarks;

    @ManyToOne
    @JoinColumn(name = "ENT_ID")
    @JsonBackReference(Enterprise.CONTACTS_ATTR_NAME)
    @NotNull(message = "MŚP nie może być puste")
    private Enterprise enterprise;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    public String getContactData() {
        return contactData;
    }

    public void setContactData(String contactData) {
        this.contactData = contactData;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
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
        return Objects.equals(id, ((EnterpriseContact) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }
}
