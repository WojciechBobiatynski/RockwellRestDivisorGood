package pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.reimbursement.searchform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.searchform.ReimbursementDeliverySearchResultDTO;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementDelivery;
import pl.sodexo.it.gryf.service.mapping.entityToDto.GryfEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.dictionaries.DictionaryEntityMapper;

/**
 * Created by akmiecinski on 2016-09-28.
 */
@Component
public class ReimbursementDeliveryEntityToSearchResultMapper extends GryfEntityMapper<ReimbursementDelivery, ReimbursementDeliverySearchResultDTO> {

    @Autowired
    private DictionaryEntityMapper dictionaryEntityMapper;
    
    @Override
    protected ReimbursementDeliverySearchResultDTO initDestination() {
        return new ReimbursementDeliverySearchResultDTO();
    }

    @Override
    public void map(ReimbursementDelivery entity, ReimbursementDeliverySearchResultDTO dto) {
        super.map(entity, dto);
        dto.setId(entity.getId());
        dto.setStatus(dictionaryEntityMapper.convert(entity.getStatus()));
        if(entity.getTrainingInstitution() != null) {
            dto.setTrainingInstitutionId(entity.getTrainingInstitution().getId());
            dto.setTrainingInstitutionName(entity.getTrainingInstitution().getName());
            dto.setTrainingInstitutionVatRegNum(entity.getTrainingInstitution().getVatRegNum());
        }
        dto.setDeliveryAddress(entity.getDeliveryAddress());
        dto.setDeliveryZipCode(entity.getDeliveryZipCode());
        dto.setDeliveryCity(entity.getDeliveryCityName());
        dto.setPlannedReceiptDate(entity.getPlannedReceiptDate());
        dto.setRequestDate(entity.getRequestDate());
        dto.setDeliveryDate(entity.getDeliveryDate());
        dto.setReimbursementPattern(dictionaryEntityMapper.convert(entity.getReimbursementPattern()));
        dto.setReimbursementAnnouncementDate(entity.getReimbursementAnnouncementDate());
    }

}
