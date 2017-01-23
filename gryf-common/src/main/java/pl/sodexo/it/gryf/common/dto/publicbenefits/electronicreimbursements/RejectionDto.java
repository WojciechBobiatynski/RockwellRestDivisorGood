package pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Dto dla odrzucenia rozliczenia
 *
 * Created by akmiecinski on 30.11.2016.
 */
@ToString
public class RejectionDto implements Serializable{

    @Getter
    @Setter
    private Long ermbsId;

    @Getter
    @Setter
    @NotNull(message = "Przyczyna odrzucenia nie może być pusta")
    private Long rejectionReasonId;

    @Getter
    @Setter
    private String rejectionDetails;
}
