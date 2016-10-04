package pl.sodexo.it.gryf.model.publicbenefits.grantapplications;

import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by tomasz.bilski.ext on 2015-07-13.
 */
@ToString
@Embeddable
public class GrantApplicationAttachmentRequiredPK implements Serializable{

    //FIELDS

    @Column(name = "version_ID")
    private Long versionId;

    @Column(name = "NAME")
    private String name;

    //CONSTRUCTORS

    public GrantApplicationAttachmentRequiredPK() {
    }

    public GrantApplicationAttachmentRequiredPK(Long versionId, String name) {
        this.versionId = versionId;
        this.name = name;
    }

    //GETTERS & SETTERS

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
