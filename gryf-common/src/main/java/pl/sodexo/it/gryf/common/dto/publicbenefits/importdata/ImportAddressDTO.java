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
public class ImportAddressDTO {

    @Getter
    @Setter
    private String address;

    @Getter
    @Setter
    private String zipCode;

    @Getter
    @Setter
    private String city;

    public boolean isEmpty(){
        return Strings.isNullOrEmpty(address) &&
                Strings.isNullOrEmpty(zipCode) &&
                Strings.isNullOrEmpty(city);
    }
}
