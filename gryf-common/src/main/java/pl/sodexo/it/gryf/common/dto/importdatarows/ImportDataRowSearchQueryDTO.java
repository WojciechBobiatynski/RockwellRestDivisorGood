package pl.sodexo.it.gryf.common.dto.importdatarows;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.SearchDto;

@ToString
public class ImportDataRowSearchQueryDTO extends SearchDto {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Long jobId;

    @Getter
    @Setter
    private Integer rowNumber;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private String status;
}
