package pl.sodexo.it.gryf.model.importdata;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.mail.EmailInstance;
import pl.sodexo.it.gryf.model.mail.EmailInstanceAttachment;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by Isolution on 2016-12-01.
 */
@ToString
@Entity
@NamedQueries({
        @NamedQuery(name = "ImportDataRow.getByImportJobAndRowNum", query="select o from ImportDataRow o " +
                "where o.importJob = :importJobId " +
                "and o.rowNum = :rowNum ")
})
@Table(name = "IMPORT_DATA_ROWS", schema = "APP_PBE")
public class ImportDataRow extends VersionableEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "pk_seq")
    private Long id;

    @Column(name = "IMPORT_JOB_ID")
    private Long importJob;

    @Column(name = "ROW_NUM")
    private Integer rowNum;

    @Column(name = "DESCRIPTION")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private ImportDataRowStatus status;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "row")
    private List<ImportDataRowError> errors;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getImportJob() {
        return importJob;
    }

    public void setImportJob(Long importJob) {
        this.importJob = importJob;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImportDataRowStatus getStatus() {
        return status;
    }

    public void setStatus(ImportDataRowStatus status) {
        this.status = status;
    }


    //LIST METHODS

    private List<ImportDataRowError> getInitializedErrors() {
        if (errors == null)
            errors = new ArrayList<>();
        return errors;
    }

    public List<ImportDataRowError> getErrors() {
        return Collections.unmodifiableList(getInitializedErrors());
    }

    public void addError(ImportDataRowError error) {
        if (error.getRow() != null && error.getRow() != this) {
            error.getRow().getInitializedErrors().remove(error);
        }
        if (error.getId() == null || !getInitializedErrors().contains(error)) {
            getInitializedErrors().add(error);
        }
        error.setRow(this);
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
        return Objects.equals(id, ((ImportDataRow) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
