package pl.sodexo.it.gryf.model.publicbenefits.grantapplications;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.ToString;
import pl.sodexo.it.gryf.model.api.AuditableEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author tomasz.bilski.ext
 */
@ToString(exclude = {"application", "header"})
@Entity
@Table(name = "GRANT_APPLICATION_REP_DATA", schema = "APP_PBE")
public class GrantApplicationRepData extends AuditableEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @JsonBackReference("reportData")
    @OneToOne
    @JoinColumn(name = "ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private GrantApplication application;

    @ManyToOne
    @JoinColumn(name = "VERSION_ID", referencedColumnName = "VERSION_ID")
    private GrantApplicationRepHeader header;

    @Column(name = "VARCHAR_001")
    private String varchar001;

    @Column(name = "LVARCHAR_001")
    private String lvarchar001;

    @Column(name = "DATE_001")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date001;

    @Column(name = "NUMBER_001")
    private Number number001;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GrantApplication getApplication() {
        return application;
    }

    public void setApplication(GrantApplication grantApplication) {
        this.application = grantApplication;
    }

    public GrantApplicationRepHeader getHeader() {
        return header;
    }

    public void setHeader(GrantApplicationRepHeader versionId) {
        this.header = versionId;
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

    public Date getDate001() {
        return date001;
    }

    public void setDate001(Date date001) {
        this.date001 = date001;
    }

    public Number getNumber001() {
        return number001;
    }

    public void setNumber001(Number number001) {
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
        return Objects.equals(id, ((GrantApplicationRepData) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
