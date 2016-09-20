package pl.sodexo.it.gryf.model;

import pl.sodexo.it.gryf.utils.LoginUtils;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public class CreationAuditedEntity extends GryfEntity{

    //FIELDS

    @Column(name = "CREATED_USER")
    private String createdUser;

    @Column(name = "CREATED_TIMESTAMP")
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
        setCreatedUser(LoginUtils.getLogin());
        setCreatedTimestamp(now);
    }
}
