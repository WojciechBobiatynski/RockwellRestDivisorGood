package pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.searchform;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import pl.sodexo.it.gryf.common.dto.api.SearchDto;

import java.util.Date;

import static pl.sodexo.it.gryf.common.utils.GryfConstants.DATE_FORMAT;

/**
 * Created by Isolution on 2016-10-27.
 */
public class ContractSearchQueryDTO extends SearchDto {

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String contractTypeDescription;

    @Getter
    @Setter
    private String pesel;

    @Getter
    @Setter
    private String vatRegNum;

    @Getter
    @Setter
    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date signDateFrom;

    @Getter
    @Setter
    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date signDateTo;

    @Getter
    @Setter
    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date expiryDateFrom;

    @Getter
    @Setter
    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date expiryDateTo;

    @Getter
    @Setter
    private String grantProgramName;

    @Getter
    @Setter
    private String grantProgramOwnerName;


}
