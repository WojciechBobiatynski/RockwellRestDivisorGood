package pl.sodexo.it.gryf.model.api;

import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Isolution on 2017-01-27.
 */
@ToString
@MappedSuperclass
public class CreationAuditedEagleEntity extends GryfEntity {

    //FIELDS

    @Column(name = "CREATED")
    private String created;

    //GETTERS & SETTERS

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    //LIFECYCLE METHODS

    @PrePersist
    public void prePersist() {
        setCreated(getEagleAuditableInfo(new Date()));
    }
}
