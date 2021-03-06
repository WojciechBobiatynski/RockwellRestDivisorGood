package pl.sodexo.it.gryf.common.dto.api;

import lombok.ToString;

import java.util.Date;

/**
 * Dto dla encji CreationAuditedEntity
 *
 * Created by jbentyn on 2016-09-23.
 */
@ToString
public abstract class CreationAuditedDto extends GryfDto {

    private String createdUser;
    private Date createdTimestamp;

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
}
