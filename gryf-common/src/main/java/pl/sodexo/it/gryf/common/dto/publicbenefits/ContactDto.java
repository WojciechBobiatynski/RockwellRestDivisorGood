package pl.sodexo.it.gryf.common.dto.publicbenefits;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.AuditableDto;

/**
 * Klasa bazowa dla dto klas kontaktów
 *
 * Created by jbentyn on 2016-10-04.
 */
@ToString
public abstract class ContactDto extends AuditableDto {

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
