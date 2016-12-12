package pl.sodexo.it.gryf.common.dto.publicbenefits.importdata;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by Isolution on 2016-12-12.
 */
public class ImportAddressCorrDTO {

    @Getter
    @Setter
    @NotEmpty(message = "Adres korespondencyjny nie może być pusty")
    private String address;

    @Getter
    @Setter
    @NotEmpty(message = "Kod pocztowy adresu korespondencyjnego nie może być pusty")
    private String zipCode;

    @Getter
    @Setter
    @NotEmpty(message = "Miasto adresu korespondencyjnego nie może być puste")
    private String city;

    public boolean isEmpty(){
        return Strings.isNullOrEmpty(address) &&
                Strings.isNullOrEmpty(zipCode) &&
                Strings.isNullOrEmpty(city);
    }
}
