package pl.sodexo.it.gryf.common.dto.publicbenefits.importdata;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
}
