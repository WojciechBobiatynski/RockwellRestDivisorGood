package pl.sodexo.it.gryf.common.dto.publicbenefits.importdata;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by Isolution on 2016-12-12.
 */
public class ImportAddressCorrSplitDTO {

    @Getter
    @Setter
    @NotEmpty(message = "Ulica adresu korespondencyjnego nie może być pusta")
    private String street;

    @Getter
    @Setter
    @NotEmpty(message = "Numer domu adresu korespondencyjnego nie może być pusty")
    private String homeNumber;

    @Getter
    @Setter
    private String flatNumber;

    @Getter
    @Setter
    @NotEmpty(message = "Kod pocztowy adresu korespondencyjnego nie może być pusty")
    private String zipCode;

    @Getter
    @Setter
    @NotEmpty(message = "Miasto adresu korespondencyjnego nie może być puste")
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
