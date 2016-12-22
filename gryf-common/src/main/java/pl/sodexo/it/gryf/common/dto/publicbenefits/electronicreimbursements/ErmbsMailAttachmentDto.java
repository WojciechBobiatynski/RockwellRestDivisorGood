package pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;

/**
 * Dto załączników maili rozliczenia bonów elektronicznych
 *
 * Created by akmiecinski on 22.12.2016.
 */
@ToString
public class ErmbsMailAttachmentDto {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String originalFilename;

    @Getter
    @Setter
    private String fileLocation;

    @Getter
    @Setter
    private boolean reportFile;

    @JsonIgnore
    @Getter
    @Setter
    private FileDTO file;

    @JsonIgnore
    @Getter
    @Setter
    private boolean fileIncluded;
}
