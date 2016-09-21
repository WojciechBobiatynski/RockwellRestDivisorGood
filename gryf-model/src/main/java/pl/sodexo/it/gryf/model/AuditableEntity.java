package pl.sodexo.it.gryf.model;

import pl.sodexo.it.gryf.common.utils.LoginUtils;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public class AuditableEntity extends CreationAuditedEntity{

    //FIELDS

    @Column(name = "MODIFIED_USER")
    private String modifiedUser;

    @Column(name = "MODIFIED_TIMESTAMP")
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
        setCreatedUser(LoginUtils.getLogin());
        setCreatedTimestamp(now);
        setModifiedUser(LoginUtils.getLogin());
        setModifiedTimestamp(now);
    }
    
    @PreUpdate
    public void preUpdate() {
        setModifiedUser(LoginUtils.getLogin());
        setModifiedTimestamp(new Date());
    }

}
