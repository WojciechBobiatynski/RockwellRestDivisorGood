package pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.reimbursement.detailsform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform.ReimbursementAttachmentDTO;
import pl.sodexo.it.gryf.common.enums.YesNo;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementAttachment;
import pl.sodexo.it.gryf.service.mapping.entityToDto.GryfEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.dictionaries.DictionaryEntityMapper;

/**
 * Created by akmiecinski on 2016-09-29.
 */
@Component
public class ReimbursementAttachmentEntityMapper extends GryfEntityMapper<ReimbursementAttachment,ReimbursementAttachmentDTO> {

    @Autowired
    private DictionaryEntityMapper dictionaryEntityMapper;

    @Override
    protected ReimbursementAttachmentDTO initDestination() {
        return new ReimbursementAttachmentDTO();
    }

    @Override
    public void map(ReimbursementAttachment entity, ReimbursementAttachmentDTO dto) {
        super.map(entity, dto);
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setOriginalFileName(entity.getOriginalFileName());
        dto.setAttachmentType(dictionaryEntityMapper.convert(entity.getAttachmentType()));
        dto.setRemarks(entity.getRemarks());
        dto.setFile(null);
        dto.setMandatory(YesNo.toBoolean(entity.getRequired()));
        dto.setFileIncluded(false);
    }
}
