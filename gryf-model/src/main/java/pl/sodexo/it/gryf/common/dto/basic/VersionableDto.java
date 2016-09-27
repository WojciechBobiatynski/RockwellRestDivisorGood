package pl.sodexo.it.gryf.common.dto.basic;

import lombok.ToString;

/**
 * Dto dla encji VersionableEntity
 *
 * Created by jbentyn on 2016-09-23.
 */
@ToString
public class VersionableDto extends AuditableDto{

    private Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
