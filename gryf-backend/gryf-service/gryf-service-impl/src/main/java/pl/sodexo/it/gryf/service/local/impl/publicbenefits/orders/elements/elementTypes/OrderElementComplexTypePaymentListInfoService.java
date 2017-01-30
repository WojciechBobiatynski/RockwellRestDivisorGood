package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.elementTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementComplexTypePaymentListInfoDTO;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.invoices.PaymentRepository;
import pl.sodexo.it.gryf.model.publicbenefits.invoices.Payment;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.OrderElementBaseService;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.orders.action.OrderElementDTOProvider;

import java.util.List;

/**
 * Created by Isolution on 2017-01-30.
 */
@Service
public class OrderElementComplexTypePaymentListInfoService extends OrderElementBaseService<OrderElementComplexTypePaymentListInfoDTO> {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public OrderElementComplexTypePaymentListInfoDTO createElement(OrderElementDTOBuilder builder) {
        List<Payment> payments = paymentRepository.findByOrder(builder.getOrder().getId());
        if(payments.isEmpty()){
            return null;
        }
        return OrderElementDTOProvider.createOrderElementComplexTypePaymentListInfoDTO(builder, payments);
    }

    @Override
    public void updateValue(OrderElement element, OrderElementComplexTypePaymentListInfoDTO dto) {
    }

}
