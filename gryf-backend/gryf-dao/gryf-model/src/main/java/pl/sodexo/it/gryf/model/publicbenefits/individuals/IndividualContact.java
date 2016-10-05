package pl.sodexo.it.gryf.model.publicbenefits.individuals;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.ToString;
import pl.sodexo.it.gryf.model.publicbenefits.api.Contact;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@ToString(exclude = "individual")
@Entity
@Table(name = "INDIVIDUAL_CONTACTS", schema = "APP_PBE")
public class IndividualContact extends Contact {

    //STATIC FIELDS - ATRIBUTES

    public static final String CONTACT_DATA_ATTR_NAME = "contactData";
    public static final String INDIVIDUAL_ATTR_NAME = "individual";

    //PRIVATE FIELDS

    @ManyToOne
    @JoinColumn(name = "IND_ID")
    @JsonBackReference(Individual.CONTACTS_ATTR_NAME)
    @NotNull(message = "Osoba fizyczna nie może być pusta")
    private Individual individual;

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
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
