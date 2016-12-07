package pl.sodexo.it.gryf.common.dto.asynchjobs.searchform;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import pl.sodexo.it.gryf.common.dto.api.SearchDto;

import java.util.Date;

import static pl.sodexo.it.gryf.common.utils.GryfConstants.DATE_FORMAT;

@ToString
public class AsynchJobSearchQueryDTO extends SearchDto {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Long orderId;

    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date createdDateFrom;

    @Getter
    @Setter
    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date createdDateTo;

    @Getter
    @Setter
    private String status;

    @Getter
    @Setter
    private String createdUser;
}
