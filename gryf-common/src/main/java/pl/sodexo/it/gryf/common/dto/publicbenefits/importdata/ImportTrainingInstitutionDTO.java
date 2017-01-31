package pl.sodexo.it.gryf.common.dto.publicbenefits.importdata;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by Isolution on 2016-12-02.
 */
@ToString
public class ImportTrainingInstitutionDTO {

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
    private ImportAddressInvoiceConcatDTO addressInvoice = new ImportAddressInvoiceConcatDTO();

    @Getter
    @Setter
    @Valid
    @NotNull(message = "Pola adresu korespondencyjnego nie mogą być puste")
    private ImportAddressCorrConcatDTO addressCorr = new ImportAddressCorrConcatDTO();

    @Getter
    @Setter
    @Size(message = "Email nie może być pusty", min = 1)
    private List<String> emails;
}
