package pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.detailsform;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.common.dto.basic.AuditableDto;

import javax.validation.constraints.NotNull;

/**
 * Created by jbentyn on 2016-09-26.
 */
@ToString
public class EnterpriseContactDto extends AuditableDto {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    @NotNull(message = "Typ kontaktu nie może być pusty")
    private ContactTypeDto contactType;

    @Getter
    @Setter
    @NotEmpty(message = "Dane kontaktowe nie mogą być puste")
    private String contactData;

    @Getter
    @Setter
    private String remarks;

    @Getter
    @Setter
    private Long enterpriseId;

//    @Getter
//    @Setter
//    //    @JsonBackReference(Enterprise.CONTACTS_ATTR_NAME)
//    @NotNull(message = "MŚP nie może być puste")
//    private Enterprise enterprise;

}
