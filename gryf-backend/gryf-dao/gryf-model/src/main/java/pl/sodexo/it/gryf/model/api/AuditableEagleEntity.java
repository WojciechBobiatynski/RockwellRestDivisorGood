package pl.sodexo.it.gryf.model.api;

import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@ToString
@MappedSuperclass
public class AuditableEagleEntity extends CreationAuditedEagleEntity {

    //FIELDS

    @Column(name = "MODIFIED")
    private String modified;

    //GETTERS & SETTERS

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    //LIFECYCLE METHODS

    @PrePersist
    public void prePersist() {
        Date now = new Date();
        setCreated(getEagleAuditableInfo(now));
        setModified(getEagleAuditableInfo(now));
    }

    @PreUpdate
    public void preUpdate() {
        setModified(getEagleAuditableInfo(new Date()));
    }

}
