package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements;

import lombok.Getter;
import lombok.Setter;
import pl.sodexo.it.gryf.common.dto.publicbenefits.invoices.PaymentDTO;

import java.util.List;

/**
 * Created by Isolution on 2017-01-30.
 */
public class OrderElementComplexTypePaymentListInfoDTO extends OrderElementDTO {

    @Getter
    @Setter
    private List<PaymentDTO> payments;
}
