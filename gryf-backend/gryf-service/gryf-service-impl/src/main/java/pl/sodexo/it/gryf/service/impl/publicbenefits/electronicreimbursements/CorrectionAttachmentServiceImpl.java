package pl.sodexo.it.gryf.service.impl.publicbenefits.electronicreimbursements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CorrectionAttachmentDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ErmbsAttachmentDto;
import pl.sodexo.it.gryf.common.enums.ErmbsAttachmentStatus;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.CorrectionAttachmentRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.electronicreimbursements.EreimbursementAttachmentRepository;
import pl.sodexo.it.gryf.dao.api.search.dao.CorrectionAttachmentSearchDao;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.CorrectionAttachment;
import pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement.ErmbsAttachment;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.CorrectionAttachmentService;
import pl.sodexo.it.gryf.service.local.api.FileService;
import pl.sodexo.it.gryf.service.mapping.dtotoentity.publicbenefits.electronicreimbursements.CorrectionAttachmentDtoMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Implementacja serwisu realizującego operacje na załącznikach do korekt
 *
 * Created by akmiecinski on 30.11.2016.
 */
@Service
@Transactional
public class CorrectionAttachmentServiceImpl implements CorrectionAttachmentService {

    @Autowired
    private CorrectionAttachmentSearchDao correctionAttachmentSearchDao;

    @Autowired
    private EreimbursementAttachmentRepository ereimbursementAttachmentRepository;

    @Autowired
    private CorrectionAttachmentDtoMapper correctionAttachmentDtoMapper;

    @Autowired
    private CorrectionAttachmentRepository correctionAttachmentRepository;

    @Autowired
    private FileService fileService;

    @Override
    public FileDTO getCorrAttFileById(Long id) {
        CorrectionAttachment attachment = correctionAttachmentRepository.get(id);
        FileDTO dto = new FileDTO();
        dto.setName(attachment.getAttachmentFile().getOrginalFileName());
        dto.setInputStream(fileService.getInputStream(attachment.getAttachmentFile().getFileLocation()));
        return dto;
    }

    @Override
    public List<CorrectionAttachmentDto> createCorrAttIfNotExistsForErmbsAttBeingChanged(ElctRmbsHeadDto elctRmbsHeadDto) {
        List<CorrectionAttachmentDto> result = new ArrayList<>();
        for (ErmbsAttachmentDto ermbsAttachmentDto : elctRmbsHeadDto.getAttachments()) {
            if (ermbsAttachmentDto.getId() == null)
                continue;
            ErmbsAttachment ermbsAttachment = ereimbursementAttachmentRepository.get(ermbsAttachmentDto.getId());
            if (isAttachmentEligibleToBeCorrected(elctRmbsHeadDto, ermbsAttachmentDto, ermbsAttachment)) {
                CorrectionAttachmentDto correctionAttachment = createNewCorrectionAndFillWithData(elctRmbsHeadDto, ermbsAttachmentDto, ermbsAttachment);
                result.add(correctionAttachment);
            }
        }
        return result;
    }

    private boolean isAttachmentEligibleToBeCorrected(ElctRmbsHeadDto elctRmbsHeadDto, ErmbsAttachmentDto ermbsAttachmentDto, ErmbsAttachment ermbsAttachment) {
        return correctionAttNotExists(elctRmbsHeadDto, ermbsAttachmentDto) && ermbsAttachment.getStatus().equals(ErmbsAttachmentStatus.SENDED) && rmbsAttHasChanged(ermbsAttachmentDto, ermbsAttachment);
    }

    private CorrectionAttachmentDto createNewCorrectionAndFillWithData(ElctRmbsHeadDto elctRmbsHeadDto, ErmbsAttachmentDto ermbsAttachmentDto, ErmbsAttachment ermbsAttachment) {
        CorrectionAttachmentDto correctionAttachment = new CorrectionAttachmentDto();
        correctionAttachment.setCorrId(elctRmbsHeadDto.getLastCorrectionDto().getId());
        correctionAttachment.setErmbsAttId(ermbsAttachment.getId());
        correctionAttachment.setOldAdditionalDesc(ermbsAttachment.getAdditionalDescription());
        correctionAttachment.setOldDocumentNumber(ermbsAttachment.getDocumentNumber());
        correctionAttachment.setOldDocumentDate(ermbsAttachment.getDocumentDate());
        if(ermbsAttachment.getAttachmentFile() != null){
            correctionAttachment.setFileId(ermbsAttachment.getAttachmentFile().getId());
        }
        return correctionAttachment;
    }

    private boolean correctionAttNotExists(ElctRmbsHeadDto elctRmbsHeadDto, ErmbsAttachmentDto ermbsAttachmentDto) {
        return correctionAttachmentSearchDao.getCorrAttByAttByErmbsAttIdAndCorrId(elctRmbsHeadDto.getLastCorrectionDto().getId(), ermbsAttachmentDto.getId()) == null;
    }

    private boolean rmbsAttHasChanged(ErmbsAttachmentDto dto, ErmbsAttachment source) {
        if (dto.isChanged()) {
            return true;
        }
        if (dto.isMarkToDelete()) {
            return true;
        }
        if (documentNumberHasChanged(dto, source)) {
            return true;
        }
        if (additionalDescHasChanged(dto, source)) {
            return true;
        }
        if(documentDateHasChanged(dto, source)){
            return true;
        }
        return false;
    }

    private boolean documentNumberHasChanged(ErmbsAttachmentDto dto, ErmbsAttachment source) {
        return GryfStringUtils.isEmpty(dto.getDocumentNumber()) || !source.getDocumentNumber().equals(dto.getDocumentNumber());
    }

    private boolean documentDateHasChanged(ErmbsAttachmentDto dto, ErmbsAttachment source) {
        return !Objects.equals(dto.getDocumentDate(), source.getDocumentDate());
    }

    private boolean additionalDescHasChanged(ErmbsAttachmentDto dto, ErmbsAttachment source) {
        return (additionalDescNotEmptyForBoth(dto, source) && !source.getAdditionalDescription().equals(dto.getAdditionalDescription())) || (source.getAdditionalDescription() != null
                && dto.getAdditionalDescription() == null) || (source.getAdditionalDescription() == null && dto.getAdditionalDescription() != null);
    }

    private boolean additionalDescNotEmptyForBoth(ErmbsAttachmentDto dto, ErmbsAttachment source) {
        return !GryfStringUtils.isEmpty(dto.getAdditionalDescription()) && !GryfStringUtils.isEmpty(source.getAdditionalDescription());
    }

    @Override
    public CorrectionAttachmentDto getCorrAttByAttByErmbsAttIdAndCorrId(ElctRmbsHeadDto elctRmbsHeadDto, ErmbsAttachmentDto ermbsAttachmentDto) {
        return correctionAttachmentSearchDao.getCorrAttByAttByErmbsAttIdAndCorrId(elctRmbsHeadDto.getLastCorrectionDto().getId(), ermbsAttachmentDto.getId());
    }

    @Override
    public void saveCorrectionAttachments(List<CorrectionAttachmentDto> correctionAttachments) {
        for (CorrectionAttachmentDto correctionAttachmentDto : correctionAttachments) {
            CorrectionAttachment correctionAttachment = correctionAttachmentDtoMapper.convert(correctionAttachmentDto);
            saveOrUpdate(correctionAttachment);
        }
    }

    private void saveOrUpdate(CorrectionAttachment correctionAttachment) {
        if(correctionAttachment.getId() == null) {
            correctionAttachmentRepository.save(correctionAttachment);
        } else {
            correctionAttachmentRepository.update(correctionAttachment, correctionAttachment.getId());
        }
    }

}
