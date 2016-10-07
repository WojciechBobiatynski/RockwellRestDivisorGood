package pl.sodexo.it.gryf.common.dto.api;

import lombok.ToString;
import pl.sodexo.it.gryf.common.crud.Auditable;

import java.util.Date;

/**
 *  Dto dla encji AuditableEntity
 *
 * Created by jbentyn on 2016-09-23.
 */
@ToString
public abstract class AuditableDto extends CreationAuditedDto implements Auditable {

    private String modifiedUser;
    private Date modifiedTimestamp;

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

}
