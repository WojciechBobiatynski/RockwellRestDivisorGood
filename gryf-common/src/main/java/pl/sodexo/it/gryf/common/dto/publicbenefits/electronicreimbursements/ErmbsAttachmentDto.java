package pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.enums.ErmbsAttachmentStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto dla załączników rozliczenia bonów elektronicznych
 *
 * Created by akmiecinski on 24.11.2016.
 */
@ToString
public class ErmbsAttachmentDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private Long Id;

    @Getter
    @Setter
    private Long ermbsId;

    @Getter
    @Setter
    private Long fileId;

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private String documentNumber;

    @Getter
    @Setter
    private Date documentDate;

    @Getter
    @Setter
    private String additionalDescription;

    @Getter
    @Setter
    private String originalFileName;

    @Getter
    @Setter
    private Integer maxFileSize;

    @Getter
    @Setter
    private boolean required;

    @Getter
    @Setter
    private boolean changed;

    @Getter
    @Setter
    private Integer index;

    @Getter
    @Setter
    private boolean markToDelete;

    @Getter
    @Setter
    private ErmbsAttachmentStatus status;

    @Getter
    @Setter
    @JsonIgnoreProperties
    private FileDTO file;

    @Getter
    @Setter
    @JsonIgnoreProperties
    private FileDTO errorFile;

    @Getter
    @Setter
    private Integer version;
}
