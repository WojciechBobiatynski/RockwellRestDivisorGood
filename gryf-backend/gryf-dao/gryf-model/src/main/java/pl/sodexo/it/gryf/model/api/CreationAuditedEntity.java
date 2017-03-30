package pl.sodexo.it.gryf.model.api;

import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;

import javax.persistence.*;
import java.util.Date;

@ToString
@MappedSuperclass
public class CreationAuditedEntity extends GryfEntity {

    //FIELDS

    @Column(name = "CREATED_USER", updatable = false)
    private String createdUser;

    @Column(name = "CREATED_TIMESTAMP", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTimestamp;

    //GETTERS & SETTERS

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Date createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    //LIFECYCLE METHODS

    @PrePersist
    public void prePersist() {
        Date now = new Date();
        String auditableInfo = GryfUser.getLoggedUserAuditableInfo();

        setCreatedUser(!GryfStringUtils.isEmpty(auditableInfo) ? auditableInfo : "GRYF");
        setCreatedTimestamp(now);
    }
}
