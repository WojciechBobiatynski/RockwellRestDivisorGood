package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.elementTypes;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementComplexTypeAddressInfoDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.dao.api.crud.repository.dictionaries.ZipCodeRepository;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.ContractType;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.OrderElementBaseService;
import pl.sodexo.it.gryf.service.mapping.entitytodto.dictionaries.ZipCodeEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.orders.action.OrderElementDTOProvider;

import java.util.List;
import java.util.Objects;

/**
 * Created by Isolution on 2016-12-28.
 */
@Service
public class OrderElementComplexTypeAddressInfoService extends OrderElementBaseService<OrderElementComplexTypeAddressInfoDTO> {

    //PRIVATE FIELDS

    @Autowired
    private ZipCodeEntityMapper zipCodeEntityMapper;

    @Autowired
    private ZipCodeRepository zipCodeRepository;

    //PUBLIC METHODS

    @Override
    public OrderElementComplexTypeAddressInfoDTO createElement(OrderElementDTOBuilder builder) {
        Order order = builder.getOrder();
        Contract contract = order.getContract();

        //INVOICE FIELDS
        String addressInvoice;
        ZipCode zipCodeInvoice;
        if(contract != null) {
            addressInvoice =  ContractType.TYPE_IND.equals(contract.getContractType().getId()) ?
                    contract.getIndividual().getAddressInvoice() :
                    contract.getEnterprise().getAddressInvoice();
            zipCodeInvoice = ContractType.TYPE_IND.equals(contract.getContractType().getId()) ?
                    contract.getIndividual().getZipCodeInvoice() :
                    contract.getEnterprise().getZipCodeInvoice();
        }else{
            addressInvoice = null;
            zipCodeInvoice = null;
        }

        //CORR FIELDS
        String addressCorr = order. getAddressCorr();
        ZipCode zipCodeCorr = order.getZipCodeCorr();

        return OrderElementDTOProvider.createOrderElementComplexTypeAddressInfoDTO(builder,
                addressInvoice, zipCodeEntityMapper.convert(zipCodeInvoice),
                addressCorr, zipCodeEntityMapper.convert(zipCodeCorr));
    }

    @Override
    public void updateValue(OrderElement element, OrderElementComplexTypeAddressInfoDTO dto) {
        Order order = element.getOrder();
        order.setAddressCorr(dto.getAddressCorr());
        order.setZipCodeCorr(dto.getZipCodeCorr() != null ? zipCodeRepository.get(dto.getZipCodeCorr().getId()) : null);
    }

    //PROTECTED METHODS - OVERRIDE VALIDATION

    @Override
    protected boolean isValueInserted(OrderElement orderElement, OrderElementComplexTypeAddressInfoDTO dto){
        String val1Old = orderElement.getOrder().getAddressCorr();
        String val1New = dto.getAddressCorr();

        Long val2Old = orderElement.getOrder().getZipCodeCorr() != null ? orderElement.getOrder().getZipCodeCorr().getId() : null;
        Long val2New = dto.getZipCodeCorr() != null ?  dto.getZipCodeCorr().getId() : null;

        return (val1Old == null && val1New != null) || (val2Old == null && val2New != null);
    }

    @Override
    protected boolean isValueUpdated(OrderElement orderElement, OrderElementComplexTypeAddressInfoDTO dto){
        String val1Old = orderElement.getOrder().getAddressCorr();
        String val1New = dto.getAddressCorr();

        Long val2Old = orderElement.getOrder().getZipCodeCorr() != null ? orderElement.getOrder().getZipCodeCorr().getId() : null;
        Long val2New = dto.getZipCodeCorr() != null ?  dto.getZipCodeCorr().getId() : null;

        return !Objects.equals(val1Old, val1New) || !Objects.equals(val2Old, val2New);
    }

    @Override
    protected boolean isValueFilled(OrderElement orderElement, OrderElementComplexTypeAddressInfoDTO dto){
        String val1New = dto.getAddressCorr();
        Long val2New = dto.getZipCodeCorr() != null ?  dto.getZipCodeCorr().getId() : null;
        return !Strings.isNullOrEmpty(val1New) && (val2New != null && val2New != 0);
    }

    @Override
    protected void addViolation(List<EntityConstraintViolation> violations, OrderElementComplexTypeAddressInfoDTO dto, String message){
        violations.add(new EntityConstraintViolation(dto.getOrderFlowElementId(), message, null));
    }
}
