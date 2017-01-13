package pl.sodexo.it.gryf.common.dto.publicbenefits.importdata;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by Isolution on 2016-11-30.
 */
@ToString
public class ImportIndividualDTO {

    @Getter
    @Setter
    @NotEmpty(message = "Imię nie może być puste")
    private String firstName;

    @Getter
    @Setter
    @NotEmpty(message = "Nazwisko nie może być puste")
    private String lastName;

    @Getter
    @Setter
    @NotEmpty(message = "PESEL nie może być pusty")
    private String pesel;

    @Getter
    @Setter
    @Valid
    @NotNull(message = "Pola adresu użytkownika do faktury nie mogą być puste")
    private ImportAddressInvoiceSplitDTO addressInvoice = new ImportAddressInvoiceSplitDTO();

    @Getter
    @Setter
    @NotEmpty(message = "Email nie może być pusty")
    private String email;

}
