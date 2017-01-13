package pl.sodexo.it.gryf.common.dto.publicbenefits.importdata;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by Isolution on 2016-11-30.
 */
@ToString
public class ImportAddressInvoiceConcatDTO {

    @Getter
    @Setter
    @NotEmpty(message = "Adresu do faktury nie może być pusty")
    private String address;

    @Getter
    @Setter
    @NotEmpty(message = "Kod pocztowy adresu do faktury nie może być pusty")
    private String zipCode;

    @Getter
    @Setter
    @NotEmpty(message = "Miasto adresu do faktury nie może być puste")
    private String city;

    //EXTRA GETTERS

    public boolean isEmpty(){
        return Strings.isNullOrEmpty(address) &&
                Strings.isNullOrEmpty(zipCode) &&
                Strings.isNullOrEmpty(city);
    }
}
