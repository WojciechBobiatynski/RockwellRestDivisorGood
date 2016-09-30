package pl.sodexo.it.gryf.service.mapping.entityToDto.grantapplications.detailsform;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.detailsform.GrantApplicationAttachmentRequiredDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.detailsform.GrantApplicationVersionDictionaryDTO;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationAttachmentRequired;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationVersion;
import pl.sodexo.it.gryf.service.mapping.entityToDto.GryfEntityMapper;

import java.util.ArrayList;

/**
 * Created by akmiecinski on 2016-09-28.
 */
@Component
public class GrantApplicationVersionEntityMapper extends GryfEntityMapper<GrantApplicationVersion, GrantApplicationVersionDictionaryDTO> {

    @Override
    protected GrantApplicationVersionDictionaryDTO initDestination() {
        return new GrantApplicationVersionDictionaryDTO();
    }

    @Override
    public void map(GrantApplicationVersion entity, GrantApplicationVersionDictionaryDTO dto) {
        dto.setId(entity.getDictionaryId());
        dto.setName(entity.getDictionaryName());
        dto.setTemplateName(entity.getTemplateName());
        dto.setAttachmentRequirementList(new ArrayList<>());
        for (GrantApplicationAttachmentRequired attachmentRequired : entity.getAttachmentRequiredList()) {
            GrantApplicationAttachmentRequiredDTO arDTO = new GrantApplicationAttachmentRequiredDTO();
            arDTO.setName(attachmentRequired.getId().getName());
            arDTO.setRemarks(attachmentRequired.getRemarks());
            dto.getAttachmentRequirementList().add(arDTO);
        }
    }

}
