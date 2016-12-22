package pl.sodexo.it.gryf.service.local.api.publicbenefits.pbeproduct;

import pl.sodexo.it.gryf.common.dto.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolDto;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.Ereimbursement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstance;

import java.util.List;

/**
 * Created by Isolution on 2016-12-21.
 */
public interface PbeProductInstancePoolLocalService {

    void createPool(Order order);

    void reservePools(TrainingInstance trainingInstance);

    void usePools(TrainingInstance instance);

    void returnPools(TrainingInstance trainingInstance);

    void reimbursPools(Ereimbursement ereimbursement);

    void expirePools(Ereimbursement ereimbursement);

    /**
     * Znajduje wszystkie przeterminowane pule bonów dla kierunku kariera
     * @return lista pul bonów
     */
    List<PbeProductInstancePoolDto> findExpiredPoolInstances();
}
