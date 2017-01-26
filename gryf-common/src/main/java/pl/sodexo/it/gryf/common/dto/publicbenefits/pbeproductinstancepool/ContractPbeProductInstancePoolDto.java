package pl.sodexo.it.gryf.common.dto.publicbenefits.pbeproductinstancepool;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ToString
public class ContractPbeProductInstancePoolDto extends PbeProductInstancePoolDto {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private String externalOrderId;

    @Getter
    @Setter
    private Integer allNum;

    @Getter
    @Setter
    private Integer reservedNum;

    @Getter
    @Setter
    private Integer usedNum;

    @Getter
    @Setter
    private Integer reimbursedNum;

    @Getter
    @Setter
    private Integer expiredNum;

    @Getter
    @Setter
    private Date startDate;

}
