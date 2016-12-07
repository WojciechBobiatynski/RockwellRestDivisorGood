package pl.sodexo.it.gryf.common.dto.asynchjobs.searchform;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by Isolution on 2016-10-27.
 */
@ToString
public class AsynchJobSearchResultDTO {

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
    private String status;

    @Getter
    @Setter
    private Date createdDate;

    @Getter
    @Setter
    private String createdUser;
}
