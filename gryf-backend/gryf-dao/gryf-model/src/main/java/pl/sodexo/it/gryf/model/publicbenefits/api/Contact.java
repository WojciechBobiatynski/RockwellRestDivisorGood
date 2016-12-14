package pl.sodexo.it.gryf.model.publicbenefits.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.model.api.AuditableEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Klasa bazowa dla encji typu kontakt
 *
 * Created by jbentyn on 2016-10-04.
 */
@MappedSuperclass
@ToString
public abstract class Contact extends AuditableEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "pk_seq")
    @Getter
    @Setter
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "CONTACT_TYPE")
    @NotNull(message = "Typ kontaktu nie może być pusty")
    @Getter
    @Setter
    protected ContactType contactType;

    @Column(name = "CONTACT_DATA")
    @NotEmpty(message = "Dane kontaktowe nie mogą być puste")
    @Size(max = 300, message = "Dane kontaktowe muszą zawierać maksymalnie 300 znaków")
    @Getter
    @Setter
    protected String contactData;

    @Column(name = "REMARKS")
    @Getter
    @Setter
    protected String remarks;
}
