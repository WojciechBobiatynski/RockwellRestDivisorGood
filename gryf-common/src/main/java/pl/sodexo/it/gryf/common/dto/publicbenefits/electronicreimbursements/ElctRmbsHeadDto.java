package pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.ind.ProductHeadDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Podstawowe dto dla listy rozliczeń elektronicznych
 *
 * Created by akmiecinski on 14.11.2016.
 */
@ToString
public class ElctRmbsHeadDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private Long ermbsId;

    @Getter
    @Setter
    private Long trainingInstanceId;

    @Getter
    @Setter
    private Long grantProgramId;

    @Getter
    @Setter
    private List<ProductHeadDto> products;

    @Getter
    @Setter
    private BigDecimal sxoIndAmountDueTotal;

    @Getter
    @Setter
    private BigDecimal indSxoAmountDueTotal;

    @Getter
    @Setter
    private List<ErmbsAttachmentsDto> attachments;
}
