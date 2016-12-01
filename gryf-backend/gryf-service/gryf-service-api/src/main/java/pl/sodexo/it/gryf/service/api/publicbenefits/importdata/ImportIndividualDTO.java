package pl.sodexo.it.gryf.service.api.publicbenefits.importdata;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Isolution on 2016-11-30.
 */
@ToString
public class ImportIndividualDTO {

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;

    @Getter
    @Setter
    private String pesel;

    @Getter
    @Setter
    private ImportAddressDTO addressInvoice;

    @Getter
    @Setter
    private ImportAddressDTO addressCorr;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String bankAccount;
}
