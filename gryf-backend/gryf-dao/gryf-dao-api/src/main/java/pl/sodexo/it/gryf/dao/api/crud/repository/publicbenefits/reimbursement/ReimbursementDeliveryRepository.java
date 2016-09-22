package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.reimbursement;

import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.searchform.ReimbursementDeliverySearchQueryDTO;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementDelivery;
import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface ReimbursementDeliveryRepository extends GenericRepository<ReimbursementDelivery, Long> {

    Long findRegisterReimbursementDeliveriesCount(Long trainingInstitutionId, Date dateFrom, Date dateTo);

    Long findAnnouncedDeliveryCountInDate(Long trainingInstitutionId, Date dateFrom, Date dateTo);

    List<ReimbursementDelivery> findReimbursementDeliveries(ReimbursementDeliverySearchQueryDTO dto);

    List<ReimbursementDelivery> findReimbursableDeliveries(ReimbursementDeliverySearchQueryDTO dto);
}
