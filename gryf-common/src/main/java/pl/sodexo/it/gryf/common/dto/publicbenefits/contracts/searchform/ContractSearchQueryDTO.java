package pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.searchform;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import pl.sodexo.it.gryf.common.dto.api.SearchDto;

import java.util.Date;

import static pl.sodexo.it.gryf.common.utils.GryfConstants.DATE_FORMAT;

/**
 * Created by Isolution on 2016-10-27.
 */
@Data
public class ContractSearchQueryDTO extends SearchDto {
    private Long id;
    private String contractTypeDescription;
    private String pesel;
    private String vatRegNum;

    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date signDate;

    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date expiryDate;
    private String grantProgramName;
    private String grantProgramOwnerName;
}
