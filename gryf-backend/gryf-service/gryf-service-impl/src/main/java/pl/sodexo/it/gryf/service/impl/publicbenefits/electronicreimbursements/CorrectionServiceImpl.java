package pl.sodexo.it.gryf.service.impl.publicbenefits.electronicreimbursements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CorrectionDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CorrectionNotificationEmailParamsDto;
import pl.sodexo.it.gryf.dao.api.crud.repository.other.GryfPLSQLRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.CorrectionRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementRepository;
import pl.sodexo.it.gryf.dao.api.search.dao.CorrectionSearchDao;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.Correction;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.Ereimbursement;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramParam;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.CorrectionService;
import pl.sodexo.it.gryf.service.local.api.ParamInDateService;

import java.util.Date;
import java.util.List;

import static pl.sodexo.it.gryf.common.utils.GryfConstants.BUSINESS_DAYS_FOR_CORRECTION_PARAM_NAME;

/**
 * Implementacja serwisu realizującego operacje na korektach
 *
 * Created by akmiecinski on 30.11.2016.
 */
@Service
@Transactional
public class CorrectionServiceImpl implements CorrectionService {

    @Autowired
    private CorrectionRepository correctionRepository;

    @Autowired
    private CorrectionSearchDao correctionSearchDoa;

    @Autowired
    private EreimbursementRepository ereimbursementRepository;

    @Autowired
    private ApplicationParameters applicationParameters;

    @Autowired
    private GryfPLSQLRepository gryfPLSQLRepository;

    @Autowired
    private ParamInDateService paramInDateService;

    @Override
    public Long createAndSaveCorrection(CorrectionDto correctionDto) {
        Correction corr = createNewCorrection(correctionDto);
        correctionRepository.save(corr);
        return corr.getId();
    }

    private Correction createNewCorrection(CorrectionDto correctionDto) {
        Correction correction = new Correction();
        correction.setEreimbursement(ereimbursementRepository.get(correctionDto.getErmbsId()));
        correction.setReason(correctionDto.getCorrectionReason());
        correction.setRequiredDate(gryfPLSQLRepository.getNthBusinessDay(new Date(), getBusinessDaysForCorrection(correctionDto.getErmbsId())));
        return correction;
    }

    @Override
    public List<CorrectionDto> findCorrectionsByERmbsId(Long ermbsId) {
        return correctionSearchDoa.findCorrectionsByERmbsId(ermbsId);
    }

    @Override
    public Integer findCorrectionsNumberByErmbsId(Long ermbsId) {
        return correctionSearchDoa.findCorrectionsNumberByErmbsId(ermbsId);
    }

    @Override
    public Long completeCorrection(Long correctionId) {
        Correction correction = correctionRepository.get(correctionId);
        correction.setComplementDate(new Date());
        return correctionRepository.update(correction, correctionId).getId();
    }

    @Override
    public Date getRequiredCorrectionDate(Long ermbsId) {
        Integer businessDaysForCorrection = getBusinessDaysForCorrection(ermbsId);
        return gryfPLSQLRepository.getNthBusinessDay(new Date(), businessDaysForCorrection);
    }

    @Override
    public CorrectionNotificationEmailParamsDto findCorrNotifParamsByErmbsId(Long ermbsId) {
        return correctionSearchDoa.findCorrNotifParamsByErmbsId(ermbsId);
    }

    private Integer getBusinessDaysForCorrection(Long ermbsId) {
        Ereimbursement ereimbursement = ereimbursementRepository.get(ermbsId);
        Long grantProgramId = ereimbursement.getTrainingInstance().getGrantProgram().getId();
        GrantProgramParam dbDaysForReimbParam =  paramInDateService.findGrantProgramParam(grantProgramId, BUSINESS_DAYS_FOR_CORRECTION_PARAM_NAME, new Date(), false);
        if(dbDaysForReimbParam == null) {
            return applicationParameters.getBusinessDaysNumberForCorrection();
        }
        return Integer.parseInt(dbDaysForReimbParam.getValue());
    }
}
