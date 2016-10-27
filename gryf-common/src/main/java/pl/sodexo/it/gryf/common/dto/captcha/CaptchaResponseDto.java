package pl.sodexo.it.gryf.common.dto.captcha;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Dto odpowiedzi dla captchy
 *
 * Created by akmiecinski on 27.10.2016.
 */
@ToString
public class CaptchaResponseDto implements Serializable {

    @Getter
    @Setter
    private boolean success;

    @Getter
    @Setter
    private String challenge_ts;

    @Getter
    @Setter
    private String hostname;

    @Getter
    @Setter
    private List<String> errorCodes;

}
