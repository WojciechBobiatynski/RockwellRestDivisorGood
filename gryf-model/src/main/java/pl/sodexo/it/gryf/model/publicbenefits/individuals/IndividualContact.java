package pl.sodexo.it.gryf.model.publicbenefits.individuals;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.model.AuditableEntity;
import pl.sodexo.it.gryf.model.publicbenefits.ContactType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@ToString
@Entity
@Table(name = "INDIVIDUAL_CONTACTS", schema = "APP_PBE")
public class IndividualContact  extends AuditableEntity {

    //STATIC FIELDS - ATRIBUTES

    public static final String CONTACT_DATA_ATTR_NAME = "contactData";
    public static final String INDIVIDUAL_ATTR_NAME = "individual";

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
    @JoinColumn(name = "IND_ID")
    @JsonBackReference(Individual.CONTACTS_ATTR_NAME)
    @NotNull(message = "Osoba fizyczna nie może być pusta")
    private Individual individual;

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

    public Individual getIndividual() {
        return individual;
    }

    public void setEnterprise(Individual individual) {
        this.individual = individual;
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
        return Objects.equals(id, ((IndividualContact) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }
}
