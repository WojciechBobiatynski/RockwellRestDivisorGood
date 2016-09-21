package pl.sodexo.it.gryf.model.publicbenefits.grantapplications;

import com.fasterxml.jackson.annotation.JsonBackReference;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.model.AuditableEntity;

import javax.persistence.*;
import java.util.Objects;

/**
 *
 * @author tomasz.bilski.ext
 */
@Entity
@Table(name = "GRANT_APPLICATION_ATTACHMENTS", schema = "APP_PBE")
public class GrantApplicationAttachment extends AuditableEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "pk_seq")
    private Long id;

    @JsonBackReference("attachments")
    @ManyToOne
    @JoinColumn(name = "APPLICATION_ID", referencedColumnName = "ID")
    private GrantApplication application;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ORIGINAL_FILE_NAME")
    private String originalFileName;

    @Column(name = "FILE_LOCATION")
    private String fileLocation;

    @Column(name = "REMARKS")
    private String remarks;

    //PUBLIC METHODS

    public String getExtension() {
        return !StringUtils.isEmpty(fileLocation) ? StringUtils.findFileExtension(fileLocation) : null;
    }

    public String getFileName() {
        String extension = getExtension();
        return !StringUtils.isEmpty(extension) ? String.format("%s.%s", name, extension) : name;
    }

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

    public void setApplication(GrantApplication applicationId) {
        this.application = applicationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
        return Objects.equals(id, ((GrantApplicationAttachment) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
