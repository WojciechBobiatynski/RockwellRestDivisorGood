package pl.sodexo.it.gryf.common.dto.basic;

import java.util.Date;

/**
 * Dto dla encji CreationAuditedEntity
 *
 * Created by jbentyn on 2016-09-23.
 */
public class CreationAuditedDto extends GryfDto {

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
