package pl.sodexo.it.gryf.common.dto.publicbenefits.importdata;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Isolution on 2016-12-12.
 */
@ToString
public class ImportParamsDTO {

    @Getter
    @Setter
    private Long gramtProgramId;

    @Getter
    @Setter
    private String path;
}
