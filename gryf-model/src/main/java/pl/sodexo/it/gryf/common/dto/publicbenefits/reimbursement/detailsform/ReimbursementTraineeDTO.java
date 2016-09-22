package pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform;

import org.hibernate.validator.constraints.NotEmpty;
import pl.sodexo.it.gryf.common.dto.DictionaryDTO;
import pl.sodexo.it.gryf.common.validation.publicbenefits.reimbursement.ValidationGroupReimbursementSettleAndVerify;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTrainee;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-08.
 */
public class ReimbursementTraineeDTO {

    //FIELDS

    private Long id;

    @NotEmpty(message = "Imię i nazwisko uczestnika szkolenia nie może być puste")
    @Size(message = "Imię i nazwisko uczestnika może mieć maksymalnie 200 znaków", max = 200)
    private String traineeName;

    @NotNull(message = "Płeć uczestnika szkolenia nie może być pusta", groups = ValidationGroupReimbursementSettleAndVerify.class)
    private DictionaryDTO traineeSex;

    @Valid
    private List<ReimbursementTraineeAttachmentDTO> reimbursementTraineeAttachments;

    //CONSTRUCTORS

    public ReimbursementTraineeDTO() {
    }

    public ReimbursementTraineeDTO(ReimbursementTrainee entity) {
        this.id = entity.getId();
        this.traineeName = entity.getTraineeName();
        this.traineeSex = DictionaryDTO.create(entity.getTraineeSex());
        this.reimbursementTraineeAttachments = ReimbursementTraineeAttachmentDTO.createAttachmentList(entity.getReimbursementTraineeAttachments());
    }

    //STATIC METHODS - CREATE

    public static ReimbursementTraineeDTO create(ReimbursementTrainee entity) {
        return entity != null ? new ReimbursementTraineeDTO(entity) : null;
    }

    public static List<ReimbursementTraineeDTO> createList(List<ReimbursementTrainee> entities) {
        List<ReimbursementTraineeDTO> list = new ArrayList<>();
        for (ReimbursementTrainee entity : entities) {
            list.add(create(entity));
        }
        return list;
    }

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