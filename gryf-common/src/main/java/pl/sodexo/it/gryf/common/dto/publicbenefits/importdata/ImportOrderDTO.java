package pl.sodexo.it.gryf.common.dto.publicbenefits.importdata;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by Isolution on 2016-12-02.
 */
@ToString
public class ImportOrderDTO {

    @Getter
    @Setter
    private Long contractId;

    @Getter
    @Setter
    private String externalOrderId;

    @Getter
    @Setter
    private Date orderDate;

    @Getter
    @Setter
    private Integer productInstanceNum;
}
