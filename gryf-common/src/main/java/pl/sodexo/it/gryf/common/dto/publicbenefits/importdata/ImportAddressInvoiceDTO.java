package pl.sodexo.it.gryf.common.dto.publicbenefits.importdata;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by Isolution on 2016-11-30.
 */
@ToString
public class ImportAddressInvoiceDTO {

    @Getter
    @Setter
    @NotEmpty(message = "Ulica adresu do faktury nie może być pusta")
    private String street;

    @Getter
    @Setter
    @NotEmpty(message = "Numer domu adresu do faktury nie może być pustau")
    private String homeNumber;

    @Getter
    @Setter
    private String flatNumber;

    @Getter
    @Setter
    @NotEmpty(message = "Kod pocztowy adresu do faktury nie może być pusty")
    private String zipCode;

    @Getter
    @Setter
    @NotEmpty(message = "Miasto adresu do faktury nie może być puste")
    private String city;

    //EXTRA GETTERS

    public String getAddress(){
        if(Strings.isNullOrEmpty(flatNumber)){
            return String.format("%s, %s", street, homeNumber);
        }
        return String.format("%s, %s m.%s", street, homeNumber, flatNumber);
    }

    public boolean isEmpty(){
        return Strings.isNullOrEmpty(street) &&
                Strings.isNullOrEmpty(homeNumber) &&
                Strings.isNullOrEmpty(zipCode) &&
                Strings.isNullOrEmpty(city);
    }
}
