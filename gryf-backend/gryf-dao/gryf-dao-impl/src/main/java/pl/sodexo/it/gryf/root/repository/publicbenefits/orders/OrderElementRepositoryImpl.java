package pl.sodexo.it.gryf.root.repository.publicbenefits.orders;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.elements.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.root.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.utils.GryfUtils;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-22.
 */
@Repository
public class OrderElementRepositoryImpl extends GenericRepositoryImpl<OrderElement, Long> implements OrderElementRepository {

    @Override
    public List<OrderElementDTOBuilder> findDtoFactoryByOrderToModify(Long id) {
        TypedQuery<OrderElementDTOBuilder> query = entityManager.createNamedQuery(OrderElement.FIND_DTO_FACTORY_BY_ORDER_TO_MODIFY, OrderElementDTOBuilder.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public List<OrderElementDTOBuilder> findDtoFactoryByOrderToPreview(Long id) {
        TypedQuery<OrderElementDTOBuilder> query = entityManager.createNamedQuery(OrderElement.FIND_DTO_FACTORY_BY_ORDER_TO_PREVIEW, OrderElementDTOBuilder.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public Map<String, OrderElement> findByOrderAndElements(Long id, List<String> elements) {
        TypedQuery<OrderElement> query = entityManager.createNamedQuery(OrderElement.FIND_BY_ORDER_AND_ELEMENTS, OrderElement.class);
        query.setParameter("id", id);
        query.setParameter("elements", elements);
        List<OrderElement> elementList = query.getResultList();
        return GryfUtils.constructMap(elementList, new GryfUtils.MapConstructor<String, OrderElement>() {
            public boolean isAddToMap(OrderElement input) {
                return true;
            }
            public String getKey(OrderElement input) {
                return input.getOrderFlowElement().getElementId();
            }
        });
    }


}
