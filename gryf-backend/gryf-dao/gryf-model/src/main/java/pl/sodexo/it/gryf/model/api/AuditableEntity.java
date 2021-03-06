package pl.sodexo.it.gryf.model.api;

import lombok.ToString;
import pl.sodexo.it.gryf.common.crud.Auditable;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;

import javax.persistence.*;
import java.util.Date;

@ToString
@MappedSuperclass
public class AuditableEntity extends CreationAuditedEntity implements Auditable {

    //FIELDS

    @Column(name = "MODIFIED_USER", nullable = false)
    private String modifiedUser;

    @Column(name = "MODIFIED_TIMESTAMP", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedTimestamp;

    //CONSTRUCTORS

    public AuditableEntity() {
    }

    public AuditableEntity(String createdUser, Date createdTimestamp, String modifiedUser, Date modifiedTimestamp) {
        this.setCreatedUser(createdUser);
        this.setCreatedTimestamp(createdTimestamp);
        this.setModifiedUser(modifiedUser);
        this.setModifiedTimestamp(modifiedTimestamp);
    }


    //GETTERS & SETTERS

    public String getModifiedUser() {
        return modifiedUser;
    }

    public void setModifiedUser(String modifiedUser) {
        this.modifiedUser = modifiedUser;
    }

    public Date getModifiedTimestamp() {
        return modifiedTimestamp;
    }

    public void setModifiedTimestamp(Date modifiedTimestamp) {
        this.modifiedTimestamp = modifiedTimestamp;
    }

    //LIFECYCLE METHODS

    @PrePersist
    public void prePersist() {
        Date now = new Date();
        String auditableInfo = GryfUser.getLoggedUserAuditableInfo();

        setCreatedUser(!GryfStringUtils.isEmpty(auditableInfo) ? auditableInfo : "GRYF");
        setCreatedTimestamp(now);
        setModifiedUser(!GryfStringUtils.isEmpty(auditableInfo) ? auditableInfo : "GRYF");
        setModifiedTimestamp(now);
    }

    @PreUpdate
    public void preUpdate() {
        Date now = new Date();
        String auditableInfo = GryfUser.getLoggedUserAuditableInfo();

        setModifiedUser(!GryfStringUtils.isEmpty(auditableInfo) ? auditableInfo : "GRYF");
        setModifiedTimestamp(now);
    }

}
