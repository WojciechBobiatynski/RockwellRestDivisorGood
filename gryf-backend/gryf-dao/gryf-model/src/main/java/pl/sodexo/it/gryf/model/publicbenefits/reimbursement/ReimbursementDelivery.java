package pl.sodexo.it.gryf.model.publicbenefits.reimbursement;

import lombok.ToString;
import org.eclipse.persistence.annotations.OptimisticLocking;
import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;

import javax.persistence.*;
import java.util.*;

/**
 * Created by tomasz.bilski.ext on 2015-09-02.
 */
@ToString(exclude = {"reimbursementPattern", "status", "trainingInstitution", "masterReimbursementDelivery", "reimbursements"})
@Entity
@Table(name = "REIMBURSEMENT_DELIVERIES", schema = "APP_PBE")
@SequenceGenerator(name="pbe_reimb_del_seq", schema = "eagle", sequenceName = "pbe_reimb_del_seq", allocationSize = 1)
@NamedQueries(
        {@NamedQuery(name = ReimbursementDelivery.FIND_ANNOUNCED_DELIVERY_COUNT_IN_DATE, query="select count(rd) from ReimbursementDelivery rd " +
                "where rd.trainingInstitution.id = :trainingInstitutionId " +
                "and rd.status.statusId in :statuses " +
                "and rd.masterReimbursementDelivery is null " +
                "and :dateFrom <= rd.reimbursementAnnouncementDate " +
                "and rd.reimbursementAnnouncementDate <= :dateTo ")})
@OptimisticLocking(cascade=true)
public class ReimbursementDelivery extends VersionableEntity{

    //STATIC FIELDS - QUERY
    public static final String FIND_ANNOUNCED_DELIVERY_COUNT_IN_DATE = "ReimbursementDelivery.findAnnouncedDeliveryCountInDate";

    //STATIC FIELDS - ATRIBUTES
    public static final String ID_ATTR_NAME = "id";
    public static final String STATUS_ATTR_NAME = "status";
    public static final String TRAINING_INSTITUTION_ATTR_NAME = "trainingInstitution";
    public static final String PLANNED_RECEIPT_DATE_ATTR_NAME = "plannedReceiptDate";
    public static final String REQUEST_DATE_ATTR_NAME = "requestDate";
    public static final String DELIVERY_ADDRESS_ATTR_NAME = "deliveryAddress";
    public static final String DELIVERY_ZIP_CODE_ATTR_NAME = "deliveryZipCode";
    public static final String DELIVERY_CITY_NAME_ATTR_NAME = "deliveryCityName";
    public static final String DELIVERY_DATE_ATTR_NAME = "deliveryDate";

    //FIELDS

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "pbe_reimb_del_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "REIMBURSEMENT_PATTERN_ID")
    private ReimbursementPattern reimbursementPattern;

    @ManyToOne
    @JoinColumn(name = "STATUS_ID", referencedColumnName = "STATUS_ID")
    private ReimbursementDeliveryStatus status;

    @ManyToOne
    @JoinColumn(name = "TRAINING_INSTITUTION_ID")
    private TrainingInstitution trainingInstitution;

    @Column(name = "PLANNED_RECEIPT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date plannedReceiptDate;

    @Column(name = "REQUEST_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate;

    @Column(name = "DELIVERY_ADDRESS")
    private String deliveryAddress;

    @Column(name = "DELIVERY_ZIP_CODE")
    private String deliveryZipCode;

    @Column(name = "DELIVERY_CITY_NAME")
    private String deliveryCityName;

    @Column(name = "DELIVERY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryDate;

    @Column(name = "WAYBILL_NUMBER")
    private String waybillNumber;

    @Column(name = "REIMB_ANNOUNCEMENT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reimbursementAnnouncementDate;

    @Column(name = "REMARKS")
    private String remarks;

    @ManyToOne
    @JoinColumn(name = "MASTER_DELIVERY_ID")
    private ReimbursementDelivery masterReimbursementDelivery;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reimbursementDelivery")
    private List<Reimbursement> reimbursements;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReimbursementPattern getReimbursementPattern() {
        return reimbursementPattern;
    }

    public void setReimbursementPattern(ReimbursementPattern reimbursementPattern) {
        this.reimbursementPattern = reimbursementPattern;
    }

    public ReimbursementDeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(ReimbursementDeliveryStatus status) {
        this.status = status;
    }

    public TrainingInstitution getTrainingInstitution() {
        return trainingInstitution;
    }

    public void setTrainingInstitution(TrainingInstitution trainingInstitution) {
        this.trainingInstitution = trainingInstitution;
    }

    public Date getPlannedReceiptDate() {
        return plannedReceiptDate;
    }

    public void setPlannedReceiptDate(Date plannedReceiptDate) {
        this.plannedReceiptDate = plannedReceiptDate;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryZipCode() {
        return deliveryZipCode;
    }

    public void setDeliveryZipCode(String deliveryZipCode) {
        this.deliveryZipCode = deliveryZipCode;
    }

    public String getDeliveryCityName() {
        return deliveryCityName;
    }

    public void setDeliveryCityName(String deliveryCityName) {
        this.deliveryCityName = deliveryCityName;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getWaybillNumber() {
        return waybillNumber;
    }

    public void setWaybillNumber(String waybillNumber) {
        this.waybillNumber = waybillNumber;
    }

    public Date getReimbursementAnnouncementDate() {
        return reimbursementAnnouncementDate;
    }

    public void setReimbursementAnnouncementDate(Date reimbursementAnnouncementDate) {
        this.reimbursementAnnouncementDate = reimbursementAnnouncementDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public ReimbursementDelivery getMasterReimbursementDelivery() {
        return masterReimbursementDelivery;
    }

    public void setMasterReimbursementDelivery(ReimbursementDelivery masterReimbursementDelivery) {
        this.masterReimbursementDelivery = masterReimbursementDelivery;
    }

    //LIST METHODS

    private List<Reimbursement> getInitializedReimbursements() {
        if (reimbursements == null)
            reimbursements = new ArrayList<>();
        return reimbursements;
    }

    public List<Reimbursement> getReimbursements() {
        return Collections.unmodifiableList(getInitializedReimbursements());
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
        return Objects.equals(id, ((ReimbursementDelivery) o).id);
    }
}
