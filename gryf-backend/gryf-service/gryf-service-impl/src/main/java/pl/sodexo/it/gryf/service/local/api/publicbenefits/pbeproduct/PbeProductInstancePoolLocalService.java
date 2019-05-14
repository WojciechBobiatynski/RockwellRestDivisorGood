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

    void lowerReservationPools(TrainingInstance instance, int newReservationNum);

    void reduceUsedPools(TrainingInstance instance, int newUsedNum);

    void returnReservedPools(TrainingInstance trainingInstance);

    void usePools(TrainingInstance instance);

    void returnUsedPools(Ereimbursement ereimbursement);

    /**
     * Anulowanie użycia puli bonów w związku z anulowaniem odbytego szkolenia. Użycie jest usuwane. Bony zmieniają status na "przypisane".
     * @param trainingInstance instancja szkolenia, która jest anulowana
     */
    void cancelTrainingInstanceUsedPools(TrainingInstance trainingInstance);

    void reimbursPools(Ereimbursement ereimbursement);

    void expirePools(Long ermbsId);

    void returnAvaiablePools(Ereimbursement ereimbursement);

    void cancelReimbursPools(TrainingInstance trainingInstance);

    /**
     * Znajduje wszystkie przeterminowane pule bonów dla kierunku kariera
     * @return lista pul bonów
     */
    List<PbeProductInstancePoolDto> findExpiredPoolInstances();
}
