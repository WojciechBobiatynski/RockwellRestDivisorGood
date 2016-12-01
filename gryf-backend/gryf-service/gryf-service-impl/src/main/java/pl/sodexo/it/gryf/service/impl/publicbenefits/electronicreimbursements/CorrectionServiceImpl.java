package pl.sodexo.it.gryf.service.impl.publicbenefits.electronicreimbursements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CorrectionDto;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.other.GryfPLSQLRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.CorrectionRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementRepository;
import pl.sodexo.it.gryf.dao.api.search.dao.CorrectionSearchDao;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.Correction;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.CorrectionService;

import java.util.Date;

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
        correction.setRequiredDate(getRequiredCorrectionDate());
        return correction;
    }

    //TODO: wymagana data korekty liczona na podstawie parametru z bazy (i kalendarza dni roboczych?)
    public Date getRequiredCorrectionDate() {
        return GryfUtils.addDays(new Date(), 5);
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
}
