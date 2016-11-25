package pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;

import java.io.Serializable;

/**
 * Dto dla załączników rozliczenia bonów elektronicznych
 *
 * Created by akmiecinski on 24.11.2016.
 */
@ToString
public class ErmbsAttachmentsDto extends VersionableDto implements Serializable {

    @Getter
    @Setter
    private Long Id;

    @Getter
    @Setter
    private Long ermbsId;

    @Getter
    @Setter
    private Long corrId;

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private String documentNumber;

    @Getter
    @Setter
    private String additionalDescription;

    @Getter
    @Setter
    private String orginalFileName;

    @Getter
    @Setter
    private String fileLocation;

    @Getter
    @Setter
    private Integer maxFileSize;

    @Getter
    @Setter
    private boolean required;

    @Getter
    @Setter
    @JsonIgnoreProperties
    private FileDTO file;

    @Getter
    @Setter
    @JsonIgnoreProperties
    private FileDTO errorFile;
}
