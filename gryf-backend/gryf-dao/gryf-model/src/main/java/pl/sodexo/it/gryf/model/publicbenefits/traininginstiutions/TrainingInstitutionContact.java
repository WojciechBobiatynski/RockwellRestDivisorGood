package pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.model.publicbenefits.api.Contact;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Created by tomasz.bilski.ext on 2015-07-10.
 */
@ToString
@Entity
@Table(name = "TRAINING_INSTITUTION_CONTACTS", schema = "APP_PBE")
public class TrainingInstitutionContact extends Contact {

    //STATIC FIELDS - ATRIBUTES

    public static final String CONTACT_DATA_ATTR_NAME = "contactData";
    public static final String TRAINING_INSTITUTION_ATTR_NAME = "trainingInstitution";

    //PRIVATE FIELDS

    //
    //    @Id
    //    @Column(name = "ID")
    //    @GeneratedValue(generator = "pk_seq")
    //    @Getter
    //    @Setter
    //    private Long id;
    //
    //    @ManyToOne
    //    @JoinColumn(name = "CONTACT_TYPE")
    //    @NotNull(message = "Typ kontaktu nie może być pusty")
    //    @Getter
    //    @Setter
    //    private ContactType contactType;
    //
    //    @Column(name = "CONTACT_DATA")
    //    @NotEmpty(message = "Dane kontaktowe nie mogą być puste")
    //    @Getter
    //    @Setter
    //    private String contactData;
    //
    //    @Column(name = "REMARKS")
    //    @Getter
    //    @Setter
    //    private String remarks;

    @ManyToOne
    @JoinColumn(name = "TRAINING_INSTITUTION_ID")
    @JsonBackReference(TrainingInstitution.CONTACTS_ATTR_NAME)
    @NotNull(message = "IS nie może być puste")
    @Getter
    @Setter
    private TrainingInstitution trainingInstitution;

    //EQUALS & HASH CODE

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        return Objects.equals(id, ((TrainingInstitutionContact) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }
}

