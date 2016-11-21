package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Isolution on 2016-11-21.
 */
@ToString
public class CreateOrderDTO {

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

    @Getter
    @Setter
    private String addressInvoice;

    @Getter
    @Setter
    private String addressCorr;

    @Getter
    @Setter
    private Integer productInstanceNum;

    @Getter
    @Setter
    private BigDecimal ownContributionPercen;

    @Getter
    @Setter
    private BigDecimal ownContributionAmont;

    @Getter
    @Setter
    private BigDecimal grantAmount;

    @Getter
    @Setter
    private BigDecimal orderAmount;
}
