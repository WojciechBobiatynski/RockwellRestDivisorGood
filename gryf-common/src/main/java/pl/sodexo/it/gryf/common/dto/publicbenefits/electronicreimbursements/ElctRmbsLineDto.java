package pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements;

import lombok.Data;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ElctRmbsLineDto extends VersionableDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long ereimbursementId;

    private Long orderId;

    private Integer usedProductsNumber;

    private BigDecimal ownContributionPercentage;

    private BigDecimal indSubsidyPercentage;

    private BigDecimal sxoTiAmountDueTotal;

    private BigDecimal sxoIndAmountDueTotal;

    private BigDecimal indOwnContributionUsed;

    private BigDecimal indSubsidyValue;
}
