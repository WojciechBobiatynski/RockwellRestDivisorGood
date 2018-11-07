package pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.detailsform.EnterpriseDto;
import pl.sodexo.it.gryf.common.dto.security.RoleDto;
import pl.sodexo.it.gryf.common.dto.security.individuals.Verifiable;
import pl.sodexo.it.gryf.common.dto.zipcodes.detailsform.ZipCodeDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Dto dla encji Individuals
 *
 * Created by jbentyn on 2016-09-27.
 */
@ToString
public class IndividualDto extends VersionableDto implements Verifiable {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String accountRepayment;

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;

    @Getter
    @Setter
    private String pesel;

    @Getter
    @Setter
    private String sex;

    @Getter
    @Setter
    private String documentType;

    @Getter
    @Setter
    private String documentNumber;

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

    @Getter
    @Setter
    private String remarks;

    @Getter
    @Setter
    private String verificationCode;

    @Getter
    @Setter
    private Date lastLoginDate;

    @Getter
    @Setter
    private List<IndividualContactDto> contacts = new ArrayList<>();

    @Getter
    @Setter
    private List<EnterpriseDto> enterprises = new ArrayList<>();

    @Getter
    @Setter
    private List<RoleDto> roles = new ArrayList<>();

    @Override
    public String getLogin() {
        return pesel;
    }

    @Override
    public String getVerificationEmail() {
        return getContacts().stream().filter(individualContactDto -> "VER_EMAIL".equals(individualContactDto.getContactType().getType())).map(individualContactDto -> individualContactDto.getContactData())
                .findAny().orElse(null);
    }
}
