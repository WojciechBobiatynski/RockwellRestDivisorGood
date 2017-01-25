package pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Isolution on 2017-01-25.
 */
@ToString
public class ErmbsGrantProgramParamsDto implements Serializable {

    @Getter
    @Setter
    private Long ereimbursmentId;

    @Getter
    @Setter
    private String grantProgramName;

    @Getter
    @Setter
    private String grantProgramEmailCC;

    @Getter
    @Setter
    private String grantProgramEmailFrom;

    @Getter
    @Setter
    private String grantProgramEmailReplay;
}
