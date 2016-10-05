package pl.sodexo.it.gryf.model.publicbenefits.reimbursement;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.DictionaryEntity;
import pl.sodexo.it.gryf.model.api.GryfEntity;

import javax.persistence.*;
import java.util.*;

/**
 * Created by tomasz.bilski.ext on 2015-09-02.
 */
@ToString(exclude = {"reimbursementAttachmentRequiredList", "reimbursementTraineeAttachmentRequiredList", "reimbursementDeliveryList"})
@Entity
@Table(name = "REIMBURSEMENT_PATTERNS", schema = "APP_PBE")
@NamedQueries({
        @NamedQuery(name = ReimbursementPattern.FIND_BY_DATE, query = "select e from ReimbursementPattern e " +
                "where  (e.dateFrom is null or e.dateFrom <= :date) " +
                "and (e.dateTo is null or :date <= e.dateTo) ")})
public class ReimbursementPattern extends GryfEntity implements DictionaryEntity{

    //STATIC FIELDS - NAMED QUERY

    public static final String FIND_BY_DATE = "ReimbursementPattern.findByDate";


    //FIELDS

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;


    @Column(name = "DATE_FROM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;

    @Column(name = "DATE_TO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reimbursementPattern")
    private List<ReimbursementAttachmentRequired> reimbursementAttachmentRequiredList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reimbursementPattern")
    private List<ReimbursementTraineeAttachmentRequired> reimbursementTraineeAttachmentRequiredList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reimbursementPattern")
    private List<ReimbursementDelivery> reimbursementDeliveryList;

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

    //LIST METHODS

    private List<ReimbursementAttachmentRequired> getInitializedReimbursementAttachmentRequiredList() {
        if (reimbursementAttachmentRequiredList == null)
            reimbursementAttachmentRequiredList = new ArrayList<>();
        return reimbursementAttachmentRequiredList;
    }

    public List<ReimbursementAttachmentRequired> getReimbursementAttachmentRequiredList() {
        return Collections.unmodifiableList(getInitializedReimbursementAttachmentRequiredList());
    }

    private List<ReimbursementTraineeAttachmentRequired> getInitializedReimbursementTraineeAttachmentRequiredList() {
        if (reimbursementTraineeAttachmentRequiredList == null)
            reimbursementTraineeAttachmentRequiredList = new ArrayList<>();
        return reimbursementTraineeAttachmentRequiredList;
    }

    public List<ReimbursementTraineeAttachmentRequired> getReimbursementTraineeAttachmentRequiredList() {
        return Collections.unmodifiableList(getInitializedReimbursementTraineeAttachmentRequiredList());
    }

    private List<ReimbursementDelivery> getInitializedReimbursementDeliveryList() {
        if (reimbursementDeliveryList == null)
            reimbursementDeliveryList = new ArrayList<>();
        return reimbursementDeliveryList;
    }

    public List<ReimbursementDelivery> getReimbursementDeliveryList() {
        return Collections.unmodifiableList(getInitializedReimbursementDeliveryList());
    }

    //DICTIONARY METHODS

    @Override
    public Object getDictionaryId() {
        return id;
    }

    @Override
    public String getDictionaryName() {
        return name;
    }

    @Override
    public String getOrderField() {
        return "name";
    }

    @Override
    public String getActiveField() {
        return null;
    }

    //EQUALS & HASH CODE

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        return Objects.equals(id, ((ReimbursementPattern) o).id);
    }
}
