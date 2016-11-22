package pl.sodexo.it.gryf.model.attachments;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by akmiecinski on 22.11.2016.
 */
@ToString
@Entity
@Table(name = "ATTACHMENT_TYPES", schema = "APP_PBE")
public class AttachmentType {

    @Id
    @Column(name = "CODE")
    @Getter
    @Setter
    private String id;

    @Column(name = "NAME")
    @Getter
    @Setter
    private String name;

    @Column(name = "ORDINAL")
    @Getter
    @Setter
    private Integer ordinal;

    @Column(name = "MAX_BYTES_PER_FILE")
    @Getter
    @Setter
    private Integer maxBytesPerFile;

    @Column(name = "MAX_FILES_PER_TYPE")
    @Getter
    @Setter
    private Integer maxFilesPerType;

}
