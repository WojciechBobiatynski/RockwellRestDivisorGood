package pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by adziobek on 20.10.2016.
 *
 * DTO dla uczestnika szkolenia.
 */
@ToString
public class RmbsIndDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;

    @Getter
    @Setter
    private String pesel;

}