package pl.sodexo.it.gryf.root.service.publicbenefits.reimbursement;

import pl.sodexo.it.gryf.common.dto.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementDeliveryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.searchform.ReimbursementDeliverySearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.searchform.ReimbursementDeliverySearchResultDTO;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface ReimbursementDeliveryService {

    List<ReimbursementDeliverySearchResultDTO> findReimbursementDeliveries(ReimbursementDeliverySearchQueryDTO searchDTO);

    List<ReimbursementDeliverySearchResultDTO> findReimbursableDeliveries(ReimbursementDeliverySearchQueryDTO searchDTO);

    ReimbursementDeliveryDTO findReimbursementDelivery(Long id);

    Long saveReimbursementDelivery(ReimbursementDeliveryDTO dto);

    Long settleReimbursementDelivery(ReimbursementDeliveryDTO dto);

    Long cancelReimbursementDelivery(ReimbursementDeliveryDTO dto);

    void manageLocking(Long id);

    List<DictionaryDTO> findReimbursementPatternsDictionaries();
}
