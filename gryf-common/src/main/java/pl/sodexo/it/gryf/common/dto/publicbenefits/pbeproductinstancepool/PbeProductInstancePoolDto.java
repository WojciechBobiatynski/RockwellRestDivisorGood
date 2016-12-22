package pl.sodexo.it.gryf.common.dto.publicbenefits.pbeproductinstancepool;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;

import java.math.BigDecimal;
import java.util.Date;

@ToString
public class PbeProductInstancePoolDto extends VersionableDto {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Long orderId;

    @Getter
    @Setter
    private Integer availableNum;

    @Getter
    @Setter
    private Date expiryDate;

    @Getter
    @Setter
    private BigDecimal productValue;
}
