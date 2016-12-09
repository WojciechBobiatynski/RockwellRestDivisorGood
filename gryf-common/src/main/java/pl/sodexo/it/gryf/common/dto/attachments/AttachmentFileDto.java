package pl.sodexo.it.gryf.common.dto.attachments;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;
import pl.sodexo.it.gryf.common.enums.FileStatus;

/**
 * Dto dla plików załączników rozliczenia bonów elektronicznych
 *
 * Created by akmiecinski on 09.12.2016.
 */
@ToString
public class AttachmentFileDto extends VersionableDto{

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private FileStatus status;

    @Getter
    @Setter
    private String originalFileName;

    @Getter
    @Setter
    private String fileLocation;

 }
