package pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;
import pl.sodexo.it.gryf.common.enums.ErmbsMailType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Dto dla maili rozliczenia bonów elektronicznych
 *
 * Created by akmiecinski on 24.11.2016.
 */
@ToString
public class ErmbsMailDto extends VersionableDto implements Serializable {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Long ermbsId;

    @Getter
    @Setter
    private Long emailInstanceId;

    @Getter
    @Setter
    private ErmbsMailType emailType;

    @Getter
    @Setter
    private String emailsTo;

    @Getter
    @Setter
    private String emailSubject;

    @Getter
    @Setter
    private String emailBody;

    @Getter
    @Setter
    private List<ErmbsMailAttachmentDto> attachments = new ArrayList<>();
}
