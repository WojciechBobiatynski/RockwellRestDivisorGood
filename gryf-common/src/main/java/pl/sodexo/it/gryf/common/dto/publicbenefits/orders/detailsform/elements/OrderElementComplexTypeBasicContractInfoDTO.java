package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by Isolution on 2016-11-21.
 */
@ToString
public class OrderElementComplexTypeBasicContractInfoDTO extends OrderElementDTO {

    @Getter
    @Setter
    private Long contractId;

    @Getter
    @Setter
    private String contractTypeName;

    @Getter
    @Setter
    private Date contractSignDate;

    @Getter
    @Setter
    private Date contractExpiryDate;

    @Getter
    @Setter
    private String grantProgramName;

    @Getter
    @Setter
    private String individualFirstName;

    @Getter
    @Setter
    private String individualLastName;

    @Getter
    @Setter
    private String individualPesel;

    @Getter
    @Setter
    private String enterpriseName;

    @Getter
    @Setter
    private String enterpriseVatRegNum;

}
