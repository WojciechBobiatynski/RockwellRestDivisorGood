package pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Podstawowe dto dla rozliczeń elektronicznych niewykorzystanej puli bonów
 *
 * Created by akmiecinski on 14.11.2016.
 */
@ToString
public class UnrsvPoolRmbsDto extends VersionableDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private Long ermbsId;

    @Getter
    @Setter
    private String typeCode;

    @Getter
    @Setter
    private Long orderId;

    @Getter
    @Setter
    private Long grantProgramId;

    @Getter
    @Setter
    private PbeProductInstancePoolDto pool;

    @Getter
    @Setter
    private BigDecimal sxoTiAmountDueTotal;

    @Getter
    @Setter
    private BigDecimal sxoIndAmountDueTotal;

    @Getter
    @Setter
    private BigDecimal indTiAmountDueTotal;

    @Getter
    @Setter
    private BigDecimal indOwnContributionUsed;

    @Getter
    @Setter
    private BigDecimal indSubsidyValue;

    @Getter
    @Setter
    private String statusCode;

    @Getter
    @Setter
    private Date reimbursementDate;

    @Getter
    @Setter
    private List<ErmbsReportDto> reports;

    @Getter
    @Setter
    private ErmbsMailDto email;

    @Getter
    @Setter
    private RmbsIndDto individual;

}
