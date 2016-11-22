package pl.sodexo.it.gryf.service.impl.publicbenefits.electronicreimbursements;

import com.googlecode.ehcache.annotations.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.criteria.electronicreimbursements.ElctRmbsCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CalculationChargesParamsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @Override
    public ElctRmbsHeadDto findEcltRmbsById(Long ermbsId) {
        return electronicReimbursementsDao.findEcltRmbsById(ermbsId);
    }

    private Ereimbursement createNewEreimbursement() {
        Ereimbursement ereimbursement = new Ereimbursement();
        ereimbursement.setEreimbursementStatus(ereimbursementStatusRepository.get(GryfConstants.NEW_ERMBS_STATUS_CODE));
        return ereimbursement;
    }

    private void calculateCharges(Ereimbursement ereimbursement, Long trainingInstanceId) {
        CalculationChargesParamsDto params = electronicReimbursementsDao.findCalculationChargesParamsForTrInstId(trainingInstanceId);
        if (params == null) {
            throw new NoCalculationParamsException();
        }
        calculateSxoAmount(ereimbursement, params);
        calculateIndAmount(ereimbursement, params);

    }

    private void calculateIndAmount(Ereimbursement ereimbursement, CalculationChargesParamsDto params) {
        if (params.getMaxProductInstance() == null) {
            calculateForTraining(ereimbursement, params);
        } else {
            caluclateForExam(ereimbursement, params);
        }
    }

    private void calculateForTraining(Ereimbursement ereimbursement, CalculationChargesParamsDto params) {
        BigDecimal normalizedUsedHourPrice = new BigDecimal(params.getUsedProductsNumber()).multiply(params.getTrainingHourPrice()).divide(new BigDecimal(params.getProductInstanceForHour()));
        BigDecimal normalizedUsedProductsPrice = new BigDecimal(params.getUsedProductsNumber()).multiply(params.getProductValue());
        BigDecimal usedHoursPriceCostDifference = normalizedUsedHourPrice.subtract(normalizedUsedProductsPrice);
        BigDecimal hoursPaidWithMoney = new BigDecimal(params.getTrainingHoursNumber() - params.getUsedProductsNumber() / params.getProductInstanceForHour());
        BigDecimal supplementOfNotUsingAllProducts = params.getTrainingHourPrice().multiply(hoursPaidWithMoney);
        ereimbursement.setIndSxoAmountDueTotal(usedHoursPriceCostDifference.add(supplementOfNotUsingAllProducts));
    }

    private void caluclateForExam(Ereimbursement ereimbursement, CalculationChargesParamsDto params) {
        Integer maxProductsNumber;
        BigDecimal traningCostLimitDifference = BigDecimal.ZERO;
        if (isTrainingPriceLowerThanMaxProgramLimit(params)) {
            BigDecimal result = params.getTrainingPrice().divide(params.getProductValue(), 0, RoundingMode.HALF_UP);
            maxProductsNumber = result.intValue();
        } else {
            traningCostLimitDifference = params.getTrainingPrice().subtract(params.getProductValue().multiply(new BigDecimal(params.getMaxProductInstance())));
            maxProductsNumber = params.getMaxProductInstance();
        }
        ereimbursement.setIndSxoAmountDueTotal(new BigDecimal(maxProductsNumber - params.getUsedProductsNumber()).multiply(params.getProductValue()).add(traningCostLimitDifference));
    }

    private boolean isTrainingPriceLowerThanMaxProgramLimit(CalculationChargesParamsDto params) {
        return params.getTrainingPrice().compareTo(params.getProductValue().multiply(new BigDecimal(params.getMaxProductInstance()))) < 0;
    }

    private void calculateSxoAmount(Ereimbursement ereimbursement, CalculationChargesParamsDto params) {
        BigDecimal priceOfOneHour = params.getTrainingHourPrice().subtract(new BigDecimal(params.getUsedProductsNumber()));
        BigDecimal normalizeHourPrice = priceOfOneHour.compareTo(params.getProductValue()) < 0 ? priceOfOneHour : params.getProductValue();
        BigDecimal sxoAmount = new BigDecimal(params.getUsedProductsNumber()).multiply(normalizeHourPrice);
        ereimbursement.setSxoIndAmountDueTotal(sxoAmount);
    }

    private void setTrainingInstanceWithAppropiateStatus(Long trainingInstanceId, Ereimbursement ereimbursement) {
        TrainingInstance trainingInstance = trainingInstanceRepository.get(trainingInstanceId);
        trainingInstance.setStatus(trainingInstanceStatusRepository.get(GryfConstants.TO_REIMBURSE_TRAINING_INSTANCE_STATUS_CODE));
        ereimbursement.setTrainingInstance(trainingInstance);
    }
}
