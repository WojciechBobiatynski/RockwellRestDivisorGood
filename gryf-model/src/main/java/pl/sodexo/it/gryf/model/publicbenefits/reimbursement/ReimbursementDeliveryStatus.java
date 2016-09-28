package pl.sodexo.it.gryf.model.publicbenefits.reimbursement;

import lombok.ToString;
import pl.sodexo.it.gryf.model.DictionaryEntity;
import pl.sodexo.it.gryf.model.GryfEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Created by tomasz.bilski.ext on 2015-09-02.
 */
@ToString
@Entity
@Table(name = "REIMB_DELIVERY_STATUSES", schema = "APP_PBE")
public class ReimbursementDeliveryStatus extends GryfEntity implements DictionaryEntity{

    //STATIC FEILDS - STATUSES CODE
    public static final String ORDERED_CODE = "ORDERED";
    public static final String DELIVERED_CODE = "DELIVERED";
    public static final String SCANNED_CODE = "SCANNED";
    public static final String CANCELLED_CODE = "CANCELLED";

    //STATIC FIELDS - ATRIBUTES
    public static final String STATUS_ID_ATTR_NAME = "statusId";

    //FIELDS

    @Id
    @Column(name = "STATUS_ID")
    private String statusId;

    @Column(name = "STATUS_NAME")
    private String statusName;

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
        return Objects.equals(statusId, ((ReimbursementDeliveryStatus) o).statusId);
    }
}
