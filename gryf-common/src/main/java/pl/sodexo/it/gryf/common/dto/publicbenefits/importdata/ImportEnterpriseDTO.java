package pl.sodexo.it.gryf.common.dto.publicbenefits.importdata;

import com.google.common.base.Strings;
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
public class ImportEnterpriseDTO {

    @Getter
    @Setter
    @NotEmpty(message = "Nazwa MŚP nie może być pusta")
    private String name;

    @Getter
    @Setter
    @NotEmpty(message = "NIP dla MŚP nie może być pusty")
    private String vatRegNum;

    @Getter
    @Setter
    @Valid
    @NotNull(message = "Pola adresu MŚP do faktury nie mogą być puste")
    private ImportAddressInvoiceDTO addressInvoice = new ImportAddressInvoiceDTO();

    @Getter
    @Setter
    @NotEmpty(message = "Email dla MŚP nie może być pusty")
    private String email;

    //PUBLIC METHODS

    public boolean isEmpty(){
        return Strings.isNullOrEmpty(name) &&
                Strings.isNullOrEmpty(vatRegNum) &&
                addressInvoice.isEmpty() &&
                Strings.isNullOrEmpty(email);
    }
}
