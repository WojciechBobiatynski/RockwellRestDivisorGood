package pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.detailsform;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;
import pl.sodexo.it.gryf.common.dto.other.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.other.GrantProgramDictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.searchform.EnterpriseSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform.IndividualSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.zipcodes.detailsform.ZipCodeDto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Isolution on 2016-10-27.
 */
@ToString
public class ContractDTO extends VersionableDto {

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private Date signDate;

    @Getter
    @Setter
    private Date expiryDate;

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private String accountPayment;

    @Getter
    @Setter
    private List<String> trainingCategory;

    @Getter
    @Setter
    private DictionaryDTO contractType;

    @Getter
    @Setter
    private GrantProgramDictionaryDTO grantProgram;

    @Getter
    @Setter
    private IndividualSearchResultDTO individual;

    @Getter
    @Setter
    private EnterpriseSearchResultDTO enterprise;

    @Getter
    @Setter
    private String addressInvoice;

    @Getter
    @Setter
    private ZipCodeDto zipCodeInvoice;

    @Getter
    @Setter
    private String addressCorr;

    @Getter
    @Setter
    private ZipCodeDto zipCodeCorr;

}
