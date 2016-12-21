package pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Dto z parametrami dla maili rozliczenia bonów elektronicznych
 *
 * Created by akmiecinski on 24.11.2016.
 */
@ToString
public class ErmbsMailParamsDto implements Serializable {

    @Getter
    @Setter
    private String grantProgramName;

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;

    @Getter
    @Setter
    private String noteNo;

    @Getter
    @Setter
    private String trainingName;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private boolean enterprise;
}
