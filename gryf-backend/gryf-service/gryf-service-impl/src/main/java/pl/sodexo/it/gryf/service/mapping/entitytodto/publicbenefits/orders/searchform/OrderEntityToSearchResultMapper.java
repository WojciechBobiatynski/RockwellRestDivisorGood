package pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.orders.searchform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.searchform.OrderSearchResultDTO;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.service.mapping.entitytodto.GryfEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entitytodto.dictionaries.DictionaryEntityMapper;

import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Maper mapujący encję Enterprise na EnterpriseDto
 *
 * Created by jbentyn on 2016-09-26.
 */
@Component
public class OrderEntityToSearchResultMapper extends GryfEntityMapper<Order, OrderSearchResultDTO> {

    @Autowired
    private DictionaryEntityMapper dictionaryEntityMapper;

    @Override
    protected OrderSearchResultDTO initDestination() {
        return new OrderSearchResultDTO();
    }

    @Override
    public void map(Order entity, OrderSearchResultDTO dto) {
        super.map(entity, dto);
        dto.setId(entity.getId());
        dto.setStatus(dictionaryEntityMapper.convert(entity.getStatus()));
        dto.setOrderDate(entity.getOrderDate());
        dto.setApplicationId((entity.getApplication() != null) ? entity.getApplication().getId() : null);
        dto.setEnterpriseId((entity.getEnterprise() != null) ? entity.getEnterprise().getId() : null);
        dto.setEnterpriseName((entity.getEnterprise() != null) ? entity.getEnterprise().getName() : null);
        dto.setVatRegNum((entity.getEnterprise() != null) ? entity.getEnterprise().getVatRegNum() : null);
        dto.setIndividualId((entity.getIndividual() != null) ? entity.getIndividual().getId() : null);
        dto.setIndividualName((entity.getIndividual() != null) ? entity.getIndividual().getFirstName() + " " + entity.getIndividual().getLastName() : null);
        dto.setPesel((entity.getIndividual() != null) ? entity.getIndividual().getPesel() : null);
        dto.setOperator(entity.getOperator());
    }

    //TODO AdamK: chwilowo tak, po przeniesieniu dto do commonsów wrócę do tematu
    public void map(Order entity, OrderSearchResultDTO dto, Date minRequiredDate) {
        map(entity, dto);
        dto.setMinRequiredDate(minRequiredDate);
    }

    public List<OrderSearchResultDTO> convertFromObjects(List<Object[]> entities) {
        Function<Object[] , OrderSearchResultDTO> convertObjects
                = convertedEntities -> {
                    OrderSearchResultDTO orderSearchResultDTO = initDestination();
                    map((Order) convertedEntities[0], orderSearchResultDTO, (Date) convertedEntities[1]);
                    return orderSearchResultDTO;
                };

        return entities.stream().map(convertObjects).collect(Collectors.<OrderSearchResultDTO> toList());
    }
}
