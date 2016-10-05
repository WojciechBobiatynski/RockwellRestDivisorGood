package pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.detailsform;

import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.other.DictionaryDTO;

import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-07-03.
 */
@ToString
public class GrantApplicationVersionDictionaryDTO extends DictionaryDTO {

    //FIELDS

    private String templateName;

    private List<GrantApplicationAttachmentRequiredDTO> attachmentRequirementList;

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
