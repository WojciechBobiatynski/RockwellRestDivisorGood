package pl.sodexo.it.gryf.web.ti.response;

import java.util.Date;

public class StaleDataResponse {

    //FIELDS

    private final ResponseType responseType = ResponseType.STALE_USER_DATA;
    private final String message;
    private final boolean containObjectInfo;
    private final Object id;
    private final Integer version;
    private final String modifiedUser;
    private final Date modifiedTimestamp;

    //CONSTRUCTORS

    public StaleDataResponse(String message) {
        this(message, false, null, null, null, null);
    }

    public StaleDataResponse(String message, Object id, Integer version, String modifiedUser, Date modifiedTimestamp) {
        this(message, true, id, version, modifiedUser, modifiedTimestamp);
    }

    private StaleDataResponse(String message, boolean containObjectInfo,
                            Object id, Integer version, String modifiedUser, Date modifiedTimestamp) {
        this.message = message;
        this.containObjectInfo = containObjectInfo;
        this.id = id;
        this.version = version;
        this.modifiedUser = modifiedUser;
        this.modifiedTimestamp = modifiedTimestamp;
    }

    //GETTERS

    public ResponseType getResponseType() {
        return responseType;
    }

    public String getMessage() {
        return message;
    }

    public boolean getContainObjectInfo() {
        return containObjectInfo;
    }

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
