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
    @NotEmpty(message = "Imię nie moze być puste")
    private String firstName;

    @Getter
    @Setter
    @NotEmpty(message = "Nazwisko nie moze być puste")
    private String lastName;

    @Getter
    @Setter
    @NotEmpty(message = "PESEL nie moze być pusty")
    private String pesel;

    @Getter
    @Setter
    @NotNull(message = "Pola adresu użytkownika do faktury nie mogą być puste")
    private ImportAddressDTO addressInvoice;

    @Getter
    @Setter
    @NotNull(message = "Pola adresu użytkownika korespondencyjnego nie mogą być puste")
    private ImportAddressDTO addressCorr;

    @Getter
    @Setter
    @NotEmpty(message = "Email jest wymagany")
    private String email;

}
