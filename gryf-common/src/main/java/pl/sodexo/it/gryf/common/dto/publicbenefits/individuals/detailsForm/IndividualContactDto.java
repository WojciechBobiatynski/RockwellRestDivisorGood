package pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.basic.AuditableDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.detailsform.ContactTypeDto;

/**
 * Dto dla encji IndividualContact
 * Created by jbentyn on 2016-09-27.
 */
@ToString
public class IndividualContactDto extends AuditableDto {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private ContactTypeDto contactType;

    @Getter
    @Setter
    private String contactData;

    @Getter
    @Setter
    private String remarks;

}
