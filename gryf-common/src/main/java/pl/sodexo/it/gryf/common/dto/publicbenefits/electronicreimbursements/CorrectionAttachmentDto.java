package pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;

/**
 * Dto dla korekty
 *
 * Created by akmiecinski on 30.11.2016.
 */
@ToString
public class CorrectionAttachmentDto extends VersionableDto{

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Long corrId;

    @Getter
    @Setter
    private Long ermbsAttId;

    @Getter
    @Setter
    private Long fileId;

    @Getter
    @Setter
    private String oldDocumentNumber;

    @Getter
    @Setter
    private String oldAdditionalDesc;
}
