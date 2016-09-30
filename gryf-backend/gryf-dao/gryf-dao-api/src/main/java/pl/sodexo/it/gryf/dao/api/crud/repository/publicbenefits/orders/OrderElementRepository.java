package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElementDTOBuilder;

import java.util.List;
import java.util.Map;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface OrderElementRepository extends GenericRepository<OrderElement, Long> {

    List<OrderElementDTOBuilder> findDtoFactoryByOrderToModify(Long id);

    List<OrderElementDTOBuilder> findDtoFactoryByOrderToPreview(Long id);

    Map<String, OrderElement> findByOrderAndElements(Long id, List<String> elements);
}
