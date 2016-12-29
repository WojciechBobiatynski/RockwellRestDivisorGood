package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.zipcodes.detailsform.ZipCodeDto;

/**
 * Created by Isolution on 2016-12-28.
 */
@ToString
public class OrderElementComplexTypeAddressInfoDTO extends OrderElementDTO {

    @Getter
    @Setter
    private String addressInvoice;

    @Getter
    @Setter
    private ZipCodeDto zipCodeInvoice;

    @Getter
    @Setter
    private String addressCorr;

    @Getter
    @Setter
    private ZipCodeDto zipCodeCorr;

}
