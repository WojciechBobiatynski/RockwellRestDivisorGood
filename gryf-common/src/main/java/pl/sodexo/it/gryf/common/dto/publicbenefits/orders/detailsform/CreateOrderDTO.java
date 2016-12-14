package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Isolution on 2016-11-21.
 */
@ToString
public class CreateOrderDTO {

    @Getter
    @Setter
    @NotNull(message = "Identyfikator umowy nie może być pusty")
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
    private String zipCodeInvoice;

    @Getter
    @Setter
    private String addressCorr;

    @Getter
    @Setter
    private String zipCodeCorr;

    @Getter
    @Setter
    @NotNull(message = "Zewnętrzny identyfikator zamówienia nie może być pusty")
    @Size(max = 20, message = "Identyfikator zewnętrzny zamowienia musi zawierać maksymalnie 20 znaków")
    private String externalOrderId;

    @Getter
    @Setter
    @NotNull(message = "Data zamówienia nie może być pusta")
    private Date orderDate;

    @Getter
    @Setter
    @NotNull(message = "Ilość bonów nie może być pusta")
    private Integer productInstanceNum;

    @Getter
    @Setter
    private BigDecimal productInstanceAmount;

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
