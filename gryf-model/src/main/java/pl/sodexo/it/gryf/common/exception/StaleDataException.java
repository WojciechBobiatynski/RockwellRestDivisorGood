package pl.sodexo.it.gryf.common.exception;

import pl.sodexo.it.gryf.model.VersionableEntity;

import java.util.Date;

public class StaleDataException extends RuntimeException {

    //FIELDS

    final private Object id;

    final private Integer version;

    final private String modifiedUser;

    final private Date modifiedTimestamp;

    //CONSTRUCTORS

    public StaleDataException(Object id, VersionableEntity entity) {
        this(id, entity, null);
    }

    public StaleDataException(Object id, VersionableEntity entity, String message) {
        super(message);
        this.id = id;
        this.version = entity.getVersion();
        this.modifiedUser = entity.getModifiedUser();
        this.modifiedTimestamp = entity.getModifiedTimestamp();
    }

    //GETTERS

    public Object getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public String getModifiedUser() {
        return modifiedUser;
    }

    public Date getModifiedTimestamp() {
        return modifiedTimestamp;
    }

    
}
