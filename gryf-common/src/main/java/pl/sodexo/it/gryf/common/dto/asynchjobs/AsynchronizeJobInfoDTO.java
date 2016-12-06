package pl.sodexo.it.gryf.common.dto.asynchjobs;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Isolution on 2016-12-05.
 */
@ToString
public class AsynchronizeJobInfoDTO {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String serviceName;

    @Getter
    @Setter
    private String typeParams;

    @Getter
    @Setter
    private String params;

    @Getter
    @Setter
    private Long orderId;

}
