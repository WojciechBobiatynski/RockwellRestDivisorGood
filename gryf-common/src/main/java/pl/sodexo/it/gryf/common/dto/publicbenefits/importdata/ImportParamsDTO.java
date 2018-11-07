package pl.sodexo.it.gryf.common.dto.publicbenefits.importdata;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.other.GrantProgramDictionaryDTO;

/**
 * Created by Isolution on 2016-12-12.
 */
@ToString
public class ImportParamsDTO {

    @Getter
    @Setter
    private Long grantProgramId;

    @Getter
    @Setter
    private GrantProgramDictionaryDTO grantProgram;

    @Getter
    @Setter
    private String path;
}
