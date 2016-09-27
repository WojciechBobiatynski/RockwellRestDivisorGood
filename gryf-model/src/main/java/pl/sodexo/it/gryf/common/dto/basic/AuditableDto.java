package pl.sodexo.it.gryf.common.dto.basic;

import lombok.ToString;

import java.util.Date;

/**
 *  Dto dla encji AuditableEntity
 *
 * Created by jbentyn on 2016-09-23.
 */
@ToString
public class AuditableDto extends CreationAuditedDto {

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
