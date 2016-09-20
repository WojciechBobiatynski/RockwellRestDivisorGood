package pl.sodexo.it.gryf.root.repository.publicbenefits.orders;

import pl.sodexo.it.gryf.dto.publicbenefits.orders.searchform.OrderSearchQueryDTO;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.root.repository.GenericRepository;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface OrderRepository extends GenericRepository<Order, Long> {

    List<Object[]> findOrders(OrderSearchQueryDTO dto);

    List<Order> findByEnterpriseGrantProgramInNonFinalStatuses(Long enterpriseId, Long grantProgramId);

    Long findGrantedVoucherNumberForEntAndProgram(Long enterpriseId, Long grantProgramId);
}
