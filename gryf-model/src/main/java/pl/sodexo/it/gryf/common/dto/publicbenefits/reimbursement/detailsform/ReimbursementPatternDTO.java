package pl.sodexo.it.gryf.common.dto.publicbenefits.reimbursement.detailsform;

import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.basic.GryfDto;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementPattern;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-09.
 */
@ToString
public class ReimbursementPatternDTO extends GryfDto{

    //FIELDS

    private Long id;

    private String name;

    private Date dateFrom;

    private Date dateTo;

    private Integer reimbursementDelay;

    private List<ReimbursementAttachmentDTO> reimbursementAttachmentRequiredList;

    private List<ReimbursementTraineeAttachmentDTO> reimbursementTraineeAttachmentRequiredList;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public Integer getReimbursementDelay() {
        return reimbursementDelay;
    }

    public void setReimbursementDelay(Integer reimbursementDelay) {
        this.reimbursementDelay = reimbursementDelay;
    }

    public List<ReimbursementAttachmentDTO> getReimbursementAttachmentRequiredList() {
        return reimbursementAttachmentRequiredList;
    }

    public void setReimbursementAttachmentRequiredList(List<ReimbursementAttachmentDTO> reimbursementAttachmentRequiredList) {
        this.reimbursementAttachmentRequiredList = reimbursementAttachmentRequiredList;
    }

    public List<ReimbursementTraineeAttachmentDTO> getReimbursementTraineeAttachmentRequiredList() {
        return reimbursementTraineeAttachmentRequiredList;
    }

    public void setReimbursementTraineeAttachmentRequiredList(List<ReimbursementTraineeAttachmentDTO> reimbursementTraineeAttachmentRequiredList) {
        this.reimbursementTraineeAttachmentRequiredList = reimbursementTraineeAttachmentRequiredList;
    }
}
