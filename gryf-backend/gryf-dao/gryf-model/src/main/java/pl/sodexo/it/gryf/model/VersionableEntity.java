package pl.sodexo.it.gryf.model;

import lombok.ToString;
import pl.sodexo.it.gryf.common.crud.Versionable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Created by Tomasz.Bilski on 2015-06-09.
 */
@ToString
@MappedSuperclass
public class VersionableEntity extends AuditableEntity implements Versionable {

    @Column(name = "VERSION")
    @Version
    private Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
