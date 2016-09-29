package pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform;

import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.common.dto.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.basic.GryfDto;
import pl.sodexo.it.gryf.common.validation.publicbenefits.reimbursement.ValidationGroupReimbursementSettleAndVerify;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-08.
 */
@ToString
public class ReimbursementTraineeDTO extends GryfDto {

    //FIELDS

    private Long id;

    @NotEmpty(message = "Imię i nazwisko uczestnika szkolenia nie może być puste")
    @Size(message = "Imię i nazwisko uczestnika może mieć maksymalnie 200 znaków", max = 200)
    private String traineeName;

    @NotNull(message = "Płeć uczestnika szkolenia nie może być pusta", groups = ValidationGroupReimbursementSettleAndVerify.class)
    private DictionaryDTO traineeSex;

    @Valid
    private List<ReimbursementTraineeAttachmentDTO> reimbursementTraineeAttachments;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTraineeName() {
        return traineeName;
    }

    public void setTraineeName(String traineeName) {
        this.traineeName = traineeName;
    }

    public DictionaryDTO getTraineeSex() {
        return traineeSex;
    }

    public void setTraineeSex(DictionaryDTO traineeSex) {
        this.traineeSex = traineeSex;
    }

    public List<ReimbursementTraineeAttachmentDTO> getReimbursementTraineeAttachments() {
        return reimbursementTraineeAttachments;
    }

    public void setReimbursementTraineeAttachments(List<ReimbursementTraineeAttachmentDTO> reimbursementTraineeAttachments) {
        this.reimbursementTraineeAttachments = reimbursementTraineeAttachments;
    }
}
