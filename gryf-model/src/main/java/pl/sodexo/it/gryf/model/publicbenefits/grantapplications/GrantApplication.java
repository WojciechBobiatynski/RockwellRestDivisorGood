package pl.sodexo.it.gryf.model.publicbenefits.grantapplications;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import pl.sodexo.it.gryf.model.VersionableEntity;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;

import javax.persistence.*;
import java.util.*;

/**
 *
 * @author tomasz.bilski.ext
 */
@Entity
@Table(name = "GRANT_APPLICATIONS", schema = "APP_PBE")
@SequenceGenerator(name="ga_seq", schema = "eagle", sequenceName = "ga_seq", allocationSize = 1)
public class GrantApplication extends VersionableEntity {

    //STATIC FIELDS - ATRIBUTES
    public static final String ID_ATTR_NAME = "id";
    public static final String STATUS_ATTR_NAME = "status";
    public static final String ENTERPRISE_ATTR_NAME = "enterprise";
    public static final String BASIC_DATA_ATTR_NAME = "basicData";
    public static final String ENTERPRISE_NAME_ATTR_NAME = "enterpriseName";
    public static final String APPLY_DATE_ATTR_NAME = "applyDate";
    public static final String CONSIDERATION_DATE_ATTR_NAME = "considerationDate";

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "ga_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "GRANT_PROGRAM_ID", referencedColumnName = "ID")
    private GrantProgram program;

    @JsonBackReference("applications")
    @ManyToOne
    @JoinColumn(name = "VERSION_ID", referencedColumnName = "ID")
    private GrantApplicationVersion applicationVersion;

    @ManyToOne
    @JoinColumn(name = "ENTERPRISE_ID", referencedColumnName = "ID")
    private Enterprise enterprise;

    @ManyToOne
    @JoinColumn(name = "STATUS_ID", referencedColumnName = "ID")
    private GrantApplicationStatus status;

    @Column(name = "APPLY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date applyDate;

    @Column(name = "CONSIDERATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date considerationDate;

    @Column(name = "REJECTION_REASON")
    private String rejectionReason;

    @JsonManagedReference("basicData")
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "application")
    private GrantApplicationBasicData basicData;

    @JsonManagedReference("formData")
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "application")
    private GrantApplicationFormData formData;

    @JsonManagedReference("reportData")
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "application")
    private GrantApplicationRepData reportData;

    @JsonManagedReference("attachments")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "application", orphanRemoval = true)
    private List<GrantApplicationAttachment> attachments;

    @JsonManagedReference("order")
    @OneToOne(mappedBy = "application")
    private Order order;

    @Column(name = "RECEIPT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receiptDate;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GrantProgram getProgram() {
        return program;
    }

    public void setProgram(GrantProgram program) {
        this.program = program;
    }

    public GrantApplicationVersion getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(GrantApplicationVersion applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    public GrantApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(GrantApplicationStatus status) {
        this.status = status;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Date getConsiderationDate() {
        return considerationDate;
    }

    public void setConsiderationDate(Date considerationDate) {
        this.considerationDate = considerationDate;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public GrantApplicationBasicData getBasicData() {
        return basicData;
    }

    public void setBasicData(GrantApplicationBasicData basicData) {
        this.basicData = basicData;
    }

    public GrantApplicationFormData getFormData() {
        return formData;
    }

    public void setFormData(GrantApplicationFormData formData) {
        this.formData = formData;
    }

    public GrantApplicationRepData getReportData() {
        return reportData;
    }

    public void setReportData(GrantApplicationRepData reportData) {
        this.reportData = reportData;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    //LIST METHODS

    private List<GrantApplicationAttachment> getInitializedAttachments() {
        if (attachments == null)
            attachments = new ArrayList<>();
        return attachments;
    }

    public List<GrantApplicationAttachment> getAttachments() {
        return Collections.unmodifiableList(getInitializedAttachments());
    }

    public void addAttachment(GrantApplicationAttachment attachment) {
        if (attachment.getApplication() != null && attachment.getApplication() != this) {
            attachment.getApplication().getInitializedAttachments().remove(attachment);
        }
        if (attachment.getId() == null || !getInitializedAttachments().contains(attachment)) {
            getInitializedAttachments().add(attachment);
        }
        attachment.setApplication(this);
    }

    public void removeAttachment(GrantApplicationAttachment attachment) {
        getInitializedAttachments().remove(attachment);
    }

    //EQUALS & HASH CODE

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        return Objects.equals(id, ((GrantApplication) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }


}
