package pl.sodexo.it.gryf.service.impl.publicbenefits.electronicreimbursements;

import com.googlecode.ehcache.annotations.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.criteria.electronicreimbursements.ElctRmbsCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CalculationChargesParamsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsDto;
import pl.sodexo.it.gryf.common.exception.NoCalculationParamsException;
import pl.sodexo.it.gryf.common.utils.GryfConstants;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementStatusRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstanceStatusRepository;
import pl.sodexo.it.gryf.dao.api.search.dao.ElectronicReimbursementsDao;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.Ereimbursement;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstance;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ElectronicReimbursementsService;

import java.util.List;

/**
 * Serwis implementujący operacje na e-rozliczeniach
 *
 * Created by akmiecinski on 14.11.2016.
 */
@Service
@Transactional
public class ElectronicReimbursementsServiceImpl implements ElectronicReimbursementsService {

    @Autowired
    private ElectronicReimbursementsDao electronicReimbursementsDao;

    @Autowired
    private EreimbursementRepository ereimbursementRepository;

    @Autowired
    private EreimbursementStatusRepository ereimbursementStatusRepository;

    @Autowired
    private TrainingInstanceRepository trainingInstanceRepository;

    @Autowired
    private TrainingInstanceStatusRepository trainingInstanceStatusRepository;

    @Override
    public List<ElctRmbsDto> findEcltRmbsListByCriteria(ElctRmbsCriteria criteria) {
        return electronicReimbursementsDao.findEcltRmbsListByCriteria(criteria);
    }

    @Override
    @Cacheable(cacheName = "elctRmbsStatuses")
    public List<SimpleDictionaryDto> findElctRmbsStatuses() {
        return electronicReimbursementsDao.findElctRmbsStatuses();
    }

    @Override
    public Long createRmbsByTrainingInstanceId(Long trainingInstanceId) {
        Ereimbursement ereimbursement = createNewEreimbursement();
        calculateCharges(ereimbursement, trainingInstanceId);
        setTrainingInstanceWithAppropiateStatus(trainingInstanceId, ereimbursement);
        return ereimbursementRepository.save(ereimbursement).getId();
    }

    private Ereimbursement createNewEreimbursement() {
        Ereimbursement ereimbursement = new Ereimbursement();
        ereimbursement.setEreimbursementStatus(ereimbursementStatusRepository.get(GryfConstants.NEW_ERMBS_STATUS_CODE));
        return ereimbursement;
    }

    private void calculateCharges(Ereimbursement ereimbursement, Long trainingInstanceId){
        CalculationChargesParamsDto params = electronicReimbursementsDao.findCalculationChargesParamsForTrInstId(trainingInstanceId);
        if(params == null){
            throw new NoCalculationParamsException();
        }
        ereimbursement.setSxoIndAmountDueTotal(params.getUsedProductsNumber().multiply(params.getProductValue()));
        ereimbursement.setIndSxoAmountDueTotal(params.getTrainingHourPrice().subtract(params.getProductValue()).multiply(params.getUsedProductsNumber()));
    }

    private void setTrainingInstanceWithAppropiateStatus(Long trainingInstanceId, Ereimbursement ereimbursement) {
        TrainingInstance trainingInstance = trainingInstanceRepository.get(trainingInstanceId);
        trainingInstance.setStatus(trainingInstanceStatusRepository.get(GryfConstants.TO_REIMBURSE_TRAINING_INSTANCE_STATUS_CODE));
        ereimbursement.setTrainingInstance(trainingInstance);
    }
}
