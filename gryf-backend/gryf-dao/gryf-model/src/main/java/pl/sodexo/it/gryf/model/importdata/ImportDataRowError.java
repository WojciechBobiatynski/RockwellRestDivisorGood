package pl.sodexo.it.gryf.model.importdata;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.model.api.VersionableEntity;

import javax.persistence.*;

/**
 * Created by Isolution on 2016-12-01.
 */
@ToString
@Entity
@Table(name = "IMPORT_DATA_ROW_ERRORS", schema = "APP_PBE")
public class ImportDataRowError extends VersionableEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "pk_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "IMPORT_DATA_ROW_ID")
    private ImportDataRow row;

    @Column(name = "PATH")
    private String path;

    @Column(name = "MESSAGE")
    private String message;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ImportDataRow getRow() {
        return row;
    }

    public void setRow(ImportDataRow row) {
        this.row = row;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
