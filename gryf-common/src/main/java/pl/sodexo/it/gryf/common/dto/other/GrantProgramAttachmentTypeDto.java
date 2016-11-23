package pl.sodexo.it.gryf.common.dto.other;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Dto dla typów załączników
 *
 * Created by akmiecinski on 23.11.2016.
 */
@ToString
public class GrantProgramAttachmentTypeDto {

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private Integer ordinal;

    @Getter
    @Setter
    private boolean required;

    @Getter
    @Setter
    private Integer maxBytesPerFile;

    @Getter
    @Setter
    private Integer maxFilesPerType;
}
