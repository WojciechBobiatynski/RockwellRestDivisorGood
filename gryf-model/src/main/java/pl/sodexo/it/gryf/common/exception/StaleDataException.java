package pl.sodexo.it.gryf.common.exception;

import pl.sodexo.it.gryf.common.crud.Versionable;

import java.util.Date;

public class StaleDataException extends RuntimeException {

    //FIELDS

    final private Object id;

    final private Integer version;

    final private String modifiedUser;

    final private Date modifiedTimestamp;

    //CONSTRUCTORS

    public StaleDataException(Object id, Versionable object) {
        this(id, object, null);
    }

    public StaleDataException(Object id, Versionable object, String message) {
        super(message);
        this.id = id;
        this.version = object.getVersion();
        this.modifiedUser = object.getModifiedUser();
        this.modifiedTimestamp = object.getModifiedTimestamp();
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
