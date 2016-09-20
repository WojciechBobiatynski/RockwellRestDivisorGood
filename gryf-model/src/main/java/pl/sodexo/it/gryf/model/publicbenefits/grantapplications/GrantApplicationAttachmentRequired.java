package pl.sodexo.it.gryf.model.publicbenefits.grantapplications;

import com.fasterxml.jackson.annotation.JsonBackReference;
import pl.sodexo.it.gryf.model.GryfEntity;

import javax.persistence.*;

/**
 * Created by tomasz.bilski.ext on 2015-07-13.
 */
@Entity
@Table(name = "GRANT_APPLICATION_ATTACH_REQ", schema = "APP_PBE")
public class GrantApplicationAttachmentRequired extends GryfEntity {

    //FIELDS

    @EmbeddedId
    private GrantApplicationAttachmentRequiredPK id;

    @JsonBackReference("attachmentRequiredList")
    @ManyToOne
    @JoinColumn(name = "VERSION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private GrantApplicationVersion applicationVersion;

    @Column(name = "REMARKS")
    private String remarks;

    //GETTERS & SETETRS

    public GrantApplicationAttachmentRequiredPK getId() {
        return id;
    }

    public void setId(GrantApplicationAttachmentRequiredPK id) {
        this.id = id;
    }

    public GrantApplicationVersion getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(GrantApplicationVersion applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

