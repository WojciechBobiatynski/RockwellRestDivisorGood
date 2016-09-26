package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.elementTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementComplexTypeEmailDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowElement;
import pl.sodexo.it.gryf.service.impl.BeanUtils;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.elementTypes.emailservices.EmailDTOService;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.OrderElementBaseClobService;

/**
 * Created by tomasz.bilski.ext on 2015-09-18.
 */
@Service
public class OrderElementComplexTypeEmailService extends OrderElementBaseClobService<OrderElementComplexTypeEmailDTO> {

    //FIELDS

    @Autowired
    private ApplicationContext context;

    //PUBLIC METHODS

    @Override
    public OrderElementComplexTypeEmailDTO createElement(OrderElementDTOBuilder builder) {

        //DTO FROM CLOB
        OrderElementComplexTypeEmailDTO dto = getOldValue(builder.getElement());
        if(dto != null){
            return new OrderElementComplexTypeEmailDTO(builder, dto);
        }

        //CREATE DTO
        String emailServiceBeanName = builder.getOrderFlowElement().getPropertyValue(OrderFlowElement.EMAIL_SERVICE_BEAN);
        if (emailServiceBeanName == null){
            throw new RuntimeException("Niewłaściwie uzupełniony parametr " + OrderFlowElement.EMAIL_SERVICE_BEAN +
                                    " dla elementu APP_PBE.ORDER_FLOW_ELEMENTS " + builder.getOrderFlowElement().getElementId());
        }
        EmailDTOService emailDTOService = (EmailDTOService) BeanUtils.findBean(context, emailServiceBeanName);
        return new OrderElementComplexTypeEmailDTO(builder, emailDTOService.createMailDTO(builder));
    }

    //PROTECTED METHODS - IUM - DTO

    @Override
    protected boolean isValueInserted(OrderElementComplexTypeEmailDTO oldDto, OrderElementComplexTypeEmailDTO newDto){
        String addressesToOld = oldDto != null ? oldDto.getAddressesTo() : null;
        String addressesToNew = newDto != null ? newDto.getAddressesTo() : null;

        String subjectOld = oldDto != null ? oldDto.getSubject() : null;
        String subjectNew = newDto != null ? newDto.getSubject() : null;

        String bodyOld = oldDto != null ? oldDto.getBody() : null;
        String bodyNew = newDto != null ? newDto.getBody() : null;

        return isValueInserted(addressesToOld, addressesToNew) ||
                isValueInserted(subjectOld, subjectNew) ||
                isValueInserted(bodyOld, bodyNew);

    }

    @Override
    protected boolean isValueUpdated(OrderElementComplexTypeEmailDTO oldDto, OrderElementComplexTypeEmailDTO newDto){
        String addressesToOld = oldDto != null ? oldDto.getAddressesTo() : null;
        String addressesToNew = newDto != null ? newDto.getAddressesTo() : null;

        String subjectOld = oldDto != null ? oldDto.getSubject() : null;
        String subjectNew = newDto != null ? newDto.getSubject() : null;

        String bodyOld = oldDto != null ? oldDto.getBody() : null;
        String bodyNew = newDto != null ? newDto.getBody() : null;

        return isValueUpdated(addressesToOld, addressesToNew) ||
                isValueUpdated(subjectOld, subjectNew) ||
                isValueUpdated(bodyOld, bodyNew);
    }

    @Override
    protected boolean isValueFilled(OrderElementComplexTypeEmailDTO newDto){
        String addressesToNew = newDto != null ? newDto.getAddressesTo() : null;
        String subjectNew = newDto != null ? newDto.getSubject() : null;
        String bodyNew = newDto != null ? newDto.getBody() : null;

        return isValueFilled(addressesToNew) &&
                isValueFilled(subjectNew) &&
                isValueFilled(bodyNew);
    }

}
