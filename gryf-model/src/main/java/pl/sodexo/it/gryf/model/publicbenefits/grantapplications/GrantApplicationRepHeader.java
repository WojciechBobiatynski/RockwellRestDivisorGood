package pl.sodexo.it.gryf.model.publicbenefits.grantapplications;

import lombok.ToString;
import pl.sodexo.it.gryf.model.GryfEntity;

import javax.persistence.*;
import java.util.Objects;

/**
 *
 * @author tomasz.bilski.ext
 */
@ToString(exclude = "applicationVersion")
@Entity
@Table(name = "GRANT_APPLICATION_REP_HEADERS", schema = "APP_PBE")
public class GrantApplicationRepHeader extends GryfEntity {

    //FIELDS

    @Id
    @Column(name = "VERSION_ID")
    private Long versionId;

    @OneToOne
    @JoinColumn(name = "VERSION_ID", referencedColumnName = "ID", insertable=false, updatable=false)
    private GrantApplicationVersion applicationVersion;

    @Column(name = "VARCHAR_001")
    private String varchar001;

    @Column(name = "LVARCHAR_001")
    private String lvarchar001;

    @Column(name = "DATE_001")
    private String date001;

    @Column(name = "NUMBER_001")
    private String number001;

    //GETTERS & SETTERS

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public GrantApplicationVersion getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(GrantApplicationVersion grantApplicationVersion) {
        this.applicationVersion = grantApplicationVersion;
    }

    public String getVarchar001() {
        return varchar001;
    }

    public void setVarchar001(String varchar001) {
        this.varchar001 = varchar001;
    }

    public String getLvarchar001() {
        return lvarchar001;
    }

    public void setLvarchar001(String lvarchar001) {
        this.lvarchar001 = lvarchar001;
    }

    public String getDate001() {
        return date001;
    }

    public void setDate001(String date001) {
        this.date001 = date001;
    }

    public String getNumber001() {
        return number001;
    }

    public void setNumber001(String number001) {
        this.number001 = number001;
    }

    //EQUALS & HASH CODE

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        return Objects.equals(versionId, ((GrantApplicationRepHeader) o).versionId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(versionId) % 102;
    }

}
