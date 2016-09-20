package pl.sodexo.it.gryf.web.response;

import java.util.Date;

public class StaleDataResponse {

    //FIELDS

    private final ResponseType responseType = ResponseType.STALE_USER_DATA;
    private final Object id;
    private final String message;
    private final Integer version;
    private final String modifiedUser;
    private final Date modifiedTimestamp;

    //CONSTRUCTORS

    public StaleDataResponse(Object id, Integer version, String modifiedUser, Date modifiedTimestamp, String message) {
        this.id = id;
        this.version = version;
        this.modifiedUser = modifiedUser;
        this.modifiedTimestamp = modifiedTimestamp;
        this.message = message;
    }

    //GETTERS

    public ResponseType getResponseType() {
        return responseType;
    }

    public Object getId() {
        return id;
    }

    public String getMessage() {
        return message;
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
