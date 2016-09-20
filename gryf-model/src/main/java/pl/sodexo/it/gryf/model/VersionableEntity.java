package pl.sodexo.it.gryf.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Created by Tomasz.Bilski on 2015-06-09.
 */
@MappedSuperclass
public class VersionableEntity extends AuditableEntity{

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
