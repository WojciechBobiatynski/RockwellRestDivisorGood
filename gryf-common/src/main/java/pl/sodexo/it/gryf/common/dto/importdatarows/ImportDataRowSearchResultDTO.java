package pl.sodexo.it.gryf.common.dto.importdatarows;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
public class ImportDataRowSearchResultDTO {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Integer rowNumber;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private String status;

    @Getter
    @Setter
    private List<String> errors;

}