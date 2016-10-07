package pl.sodexo.it.gryf.model.publicbenefits.reimbursement;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.DictionaryEntity;
import pl.sodexo.it.gryf.model.api.GryfEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 *
 * @author Michal.CHWEDCZUK.ext
 */
@ToString
@Entity
@Table(name = "REIMBURSEMENT_STATUSES", schema = "APP_PBE")
public class ReimbursementStatus extends GryfEntity implements DictionaryEntity {

    //STATIC FEILDS - STATUSES CODE
    public static final String ANNOUNCED_CODE = "ANNOUNCED";
    public static final String CORRECTED_CODE = "CORRECTED";
    public static final String TO_REIMB_CODE = "TO_REIMB";
    public static final String EXPORTED_CODE = "EXPORTED";
    public static final String FINISHED_CODE = "FINISHED";
    public static final String CANCELLED_CODE = "CANCELLED";
    public static final String TO_VERIFY_CODE = "TO_VERIFY";


    //STATIC FIELDS - ATRIBUTES
    public static final String STATUS_ID_ATTR_NAME = "statusId";

    //FIELDS

    @Id
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "STATUS_ID")
    private String statusId;

    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "STATUS_NAME")
    private String statusName;

    @Size(max = 200)
    @Column(name = "DESCRIPTION")
    private String description;

    //GETTERS & SETTERS

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //DICTIONARY METHODS

    @Override
    public Object getDictionaryId() {
        return statusId;
    }

    @Override
    public String getDictionaryName() {
        return statusName;
    }

    @Override
    public String getOrderField() {
        return "statusName";
    }

    @Override
    public String getActiveField() {
        return null;
    }

    //EQUALS & HASH CODE

    @Override
    public int hashCode() {
        return Objects.hashCode(statusId) % 102;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        return Objects.equals(statusId, ((ReimbursementStatus) o).statusId);
    }
}
