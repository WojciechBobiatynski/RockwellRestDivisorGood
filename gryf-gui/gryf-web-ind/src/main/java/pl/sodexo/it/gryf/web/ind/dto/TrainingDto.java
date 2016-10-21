package pl.sodexo.it.gryf.web.ind.dto;

import lombok.Getter;
import lombok.Setter;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;

import java.util.Date;

/**
 * Created by adziobek on 20.10.2016.
 *
 * DTO dla szkolenia.
 */
public class TrainingDto extends VersionableDto {
    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String trainingInstitutionName;

    @Getter
    @Setter
    private Integer reservedProductsCount;

    @Getter
    @Setter
    private Integer settledProductsCount;

    @Getter
    @Setter
    private Date startDate;

    @Getter
    @Setter
    private Date endDate;

    @Getter
    @Setter
    private Date orderProductsDate;
}