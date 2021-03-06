package pl.sodexo.it.gryf.model.publicbenefits.enterprises;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.ToString;
import pl.sodexo.it.gryf.model.publicbenefits.api.Contact;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Created by tomasz.bilski.ext on 2015-06-24.
 */
@ToString(exclude = { "enterprise"})
@Entity
@Table(name = "ENTERPRISE_CONTACTS", schema = "APP_PBE")
public class EnterpriseContact extends Contact {

    //STATIC FIELDS - ATRIBUTES

    public static final String CONTACT_DATA_ATTR_NAME = "contactData";
    public static final String ENTERPRISE_ATTR_NAME = "enterprise";

    //PRIVATE FIELDS

    @ManyToOne
    @JoinColumn(name = "ENT_ID")
    @JsonBackReference(Enterprise.CONTACTS_ATTR_NAME)
    @NotNull(message = "MŚP nie może być puste")
    private Enterprise enterprise;

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
