package pl.sodexo.it.gryf.model.attachments;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.enums.FileStatus;
import pl.sodexo.it.gryf.model.api.VersionableEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Encja odzwierciedlająca stan plików załączników w File Systemie
 *
 * Created by akmiecinski on 07.12.2016.
 */
@ToString(exclude = {"ereimbursement", "attachmentType"})
@Entity
@Table(name = "ATTACHMENT_FILES", schema = "APP_PBE")
@SequenceGenerator(name = "file_seq", schema = "eagle", sequenceName = "file_seq", allocationSize = 1)
public class AttachmentFile extends VersionableEntity {

    @Id
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator = "file_seq")
    @Getter
    @Setter
    private Long id;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private FileStatus filetatus;

    @Column(name = "ORIGINAL_FILE_NAME")
    @Getter
    @Setter
    private String orginalFileName;

    @Column(name = "FILE_LOCATION")
    @Getter
    @Setter
    private String fileLocation;

}
