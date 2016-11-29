package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders;

import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.searchform.OrderSearchQueryDTO;
import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface OrderRepository extends GenericRepository<Order, Long> {

    List<Object[]> findOrders(OrderSearchQueryDTO dto);

    List<Order> findByEnterpriseGrantProgramInNonFinalStatuses(Long enterpriseId, Long grantProgramId);

    Long findGrantedVoucherNumberForEntAndProgram(Long enterpriseId, Long grantProgramId);

    Integer countNotCanceledOrdersByContract(Long contractId);

    Integer sumProductInstanceNumInNotCanceledOrdersByContract(Long contractId);
}
