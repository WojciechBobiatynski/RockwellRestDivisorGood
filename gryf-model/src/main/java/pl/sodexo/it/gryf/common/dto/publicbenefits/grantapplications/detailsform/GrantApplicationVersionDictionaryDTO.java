package pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.detailsform;

import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.DictionaryDTO;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationAttachmentRequired;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationVersion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-07-03.
 */
@ToString
public class GrantApplicationVersionDictionaryDTO extends DictionaryDTO {

    //FIELDS

    private String templateName;

    private List<GrantApplicationAttachmentRequiredDTO> attachmentRequirementList;

    //CONSTRUCTORS

    protected GrantApplicationVersionDictionaryDTO(){
        super();
    }

    protected GrantApplicationVersionDictionaryDTO(GrantApplicationVersion entity) {
        super(entity);
        this.setTemplateName(entity.getTemplateName());
        this.setAttachmentRequirementList(new ArrayList<GrantApplicationAttachmentRequiredDTO>());
        for (GrantApplicationAttachmentRequired attachmentRequired : entity.getAttachmentRequiredList()) {
            GrantApplicationAttachmentRequiredDTO arDTO = new GrantApplicationAttachmentRequiredDTO();
            arDTO.setName(attachmentRequired.getId().getName());
            arDTO.setRemarks(attachmentRequired.getRemarks());
            this.getAttachmentRequirementList().add(arDTO);
        }
    }

    //STATIC METHODS - CREATE

    public static GrantApplicationVersionDictionaryDTO create(GrantApplicationVersion entity) {
        return entity != null ? new GrantApplicationVersionDictionaryDTO(entity) : null;
    }

    public static List<GrantApplicationVersionDictionaryDTO> createLists(List<? extends GrantApplicationVersion> entities) {
        List<GrantApplicationVersionDictionaryDTO> list = new ArrayList<>();
        for (GrantApplicationVersion entity : entities) {
            list.add(create(entity));
        }
        return list;
    }

    //GETTERS & SETETRS

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public List<GrantApplicationAttachmentRequiredDTO> getAttachmentRequirementList() {
        return attachmentRequirementList;
    }

    public void setAttachmentRequirementList(List<GrantApplicationAttachmentRequiredDTO> attachmentRequirementList) {
        this.attachmentRequirementList = attachmentRequirementList;
    }

}
