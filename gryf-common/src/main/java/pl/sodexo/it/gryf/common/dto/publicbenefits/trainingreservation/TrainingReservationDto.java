package pl.sodexo.it.gryf.common.dto.publicbenefits.trainingreservation;

import lombok.Getter;
import lombok.Setter;

public class TrainingReservationDto {

    @Getter
    @Setter
    private Long trainingId;

    @Getter
    @Setter
    private Long individualId;

    @Getter
    @Setter
    private Long grantProgramId;

    @Getter
    @Setter
    private Long contractId;

    @Getter
    @Setter
    private Integer toReservedNum;
}
