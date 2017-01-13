package pl.sodexo.it.gryf.common.dto.publicbenefits.importdata;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by Isolution on 2016-12-02.
 */
@ToString
@Deprecated()
public class ImportTrainingInstitutionV1DTO {

    @Getter
    @Setter
    @NotEmpty(message = "Identyfikator IS nie może być pusty")
    private String externalId;

    @Getter
    @Setter
    @NotEmpty(message = "NIP nie może być pusty")
    private String vatRegNum;

    @Getter
    @Setter
    @NotEmpty(message = "Nazwa IS nie może być pusta")
    private String name;

    @Getter
    @Setter
    @Valid
    @NotNull(message = "Pola adresu do faktury nie mogą być puste")
    private ImportAddressInvoiceSplitDTO addressInvoice = new ImportAddressInvoiceSplitDTO();

    @Getter
    @Setter
    @Valid
    @NotNull(message = "Pola adresu korespondencyjnego nie mogą być puste")
    private ImportAddressCorrSplitDTO addressCorr = new ImportAddressCorrSplitDTO();

    @Getter
    @Setter
    @NotEmpty(message = "Email nie może być pusty")
    private String email;
}
