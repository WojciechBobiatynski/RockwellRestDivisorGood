package pl.sodexo.it.gryf.common.dto.security.trainingInstitutions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.api.VersionableDto;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto reprezentujące żądania resetu hasła dla Usługodawcy
 *
 * Created by akmiecinski on 26.10.2016.
 */
@ToString
public class TiUserResetAttemptDto extends VersionableDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private String turId;

    @Getter
    @Setter
    private Long tiuId;

    @Getter
    @Setter
    private Date expiryDate;

    @Getter
    @Setter
    private boolean used;

}
