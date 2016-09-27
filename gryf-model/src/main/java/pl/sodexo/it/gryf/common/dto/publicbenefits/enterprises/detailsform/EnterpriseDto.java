package pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.detailsform;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.basic.VersionableDto;
import pl.sodexo.it.gryf.common.dto.dictionaries.zipcodes.detailsform.ZipCodeDto;

import java.util.List;

/**
 * Dto dla Encji Enterprise
 *
 * Created by jbentyn on 2016-09-26.
 */
@ToString
public class EnterpriseDto extends VersionableDto {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private String accountPayment;

    @Getter
    @Setter
    private String accountRepayment;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String vatRegNum;

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

    @Getter
    @Setter
    private String remarks;

    @Getter
    @Setter
    private List<EnterpriseContactDto> contacts;

}
