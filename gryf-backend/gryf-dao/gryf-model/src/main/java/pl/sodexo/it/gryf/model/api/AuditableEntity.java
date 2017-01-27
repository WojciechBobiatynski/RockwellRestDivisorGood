package pl.sodexo.it.gryf.model.api;

import lombok.ToString;
import pl.sodexo.it.gryf.common.crud.Auditable;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;

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
        setCreatedUser(!GryfUser.getLoggedUserLogin().isEmpty() ? GryfUser.getLoggedUserLogin() : "GRYF");
        setCreatedTimestamp(now);
        setModifiedUser(!GryfUser.getLoggedUserLogin().isEmpty() ? GryfUser.getLoggedUserLogin() : "GRYF");
        setModifiedTimestamp(now);
    }

    @PreUpdate
    public void preUpdate() {
        setModifiedUser(!GryfUser.getLoggedUserLogin().isEmpty() ? GryfUser.getLoggedUserLogin() : "GRYF");
        setModifiedTimestamp(new Date());
    }

}
