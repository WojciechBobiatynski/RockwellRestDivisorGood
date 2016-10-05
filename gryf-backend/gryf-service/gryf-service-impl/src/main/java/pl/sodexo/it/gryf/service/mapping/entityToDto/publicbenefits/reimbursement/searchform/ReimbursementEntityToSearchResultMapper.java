package pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.reimbursement.searchform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.searchform.ReimbursementSearchResultDTO;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.Reimbursement;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementDelivery;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;
import pl.sodexo.it.gryf.service.mapping.entityToDto.GryfEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.dictionaries.DictionaryEntityMapper;

/**
 * Created by akmiecinski on 2016-09-28.
 */
@Component
public class ReimbursementEntityToSearchResultMapper extends GryfEntityMapper<Reimbursement, ReimbursementSearchResultDTO> {

    @Autowired
    private DictionaryEntityMapper dictionaryEntityMapper;
    
    @Override
    protected ReimbursementSearchResultDTO initDestination() {
        return new ReimbursementSearchResultDTO();
    }

    @Override
    public void map(Reimbursement entity, ReimbursementSearchResultDTO dto) {
        super.map(entity, dto);
        dto.setId(entity.getId());
        dto.setInvoiceNumber(entity.getInvoiceNumber());
        dto.setStatus(dictionaryEntityMapper.convert(entity.getStatus()));

        if(entity.getReimbursementDelivery() != null) {
            ReimbursementDelivery reimbursementDelivery = entity.getReimbursementDelivery();
            dto.setReimbursementDeliveryId(reimbursementDelivery.getId());
            dto.setDeliveryDate(reimbursementDelivery.getDeliveryDate());

            TrainingInstitution trainingInstitution = reimbursementDelivery.getTrainingInstitution();
            if(trainingInstitution != null){
                dto.setTrainingInstitutionVatRegNum(trainingInstitution.getVatRegNum());
                dto.setTrainingInstitutionName(trainingInstitution.getName());
            }
        }
        dto.setAnnouncementDate(entity.getAnnouncementDate());
        dto.setReimbursementDate(entity.getReimbursementDate());
    }

}
