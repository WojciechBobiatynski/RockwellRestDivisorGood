package pl.sodexo.it.gryf.common.dto.publicbenefits.invoices;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Isolution on 2017-01-30.
 */
public class PaymentDTO {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Boolean used;

    @Getter
    @Setter
    private BigDecimal amount;

    @Getter
    @Setter
    private BigDecimal transferAmount;

    @Getter
    @Setter
    private Date matchDate;

    @Getter
    @Setter
    private Date paymentDate;

    @Getter
    @Setter
    private Date transferDate;

    @Getter
    @Setter
    private String transferDetail;
}
