package pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;

import java.io.Serializable;

/**
 * Dto dla załączników rozliczenia bonów elektronicznych
 *
 * Created by akmiecinski on 24.11.2016.
 */
@ToString
public class ErmbsReportDto extends VersionableDto implements Serializable {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Long ermbsId;

    @Getter
    @Setter
    private String typeName;

    @Getter
    @Setter
    private String fileLocation;

    @Getter
    @Setter
    private String documentNumber;

}
