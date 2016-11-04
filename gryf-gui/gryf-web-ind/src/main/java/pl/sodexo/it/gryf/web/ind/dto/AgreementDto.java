package pl.sodexo.it.gryf.web.ind.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by adziobek on 20.10.2016.
 *
 * DTO dla bonu/produktu.
 */
public class AgreementDto {

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String trainingCategory;

    @Getter
    @Setter
    private Date signingDate;
}