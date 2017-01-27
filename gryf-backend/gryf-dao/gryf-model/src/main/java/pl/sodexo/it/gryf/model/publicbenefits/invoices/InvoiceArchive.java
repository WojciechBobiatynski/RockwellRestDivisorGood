package pl.sodexo.it.gryf.model.publicbenefits.invoices;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.AuditableEagleEntity;
import pl.sodexo.it.gryf.model.api.BooleanConverter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Isolution on 2017-01-27.
 */
@ToString
@Entity
@Table(name = "INVOICES_ARCHIVE", schema = "APP_FIN")
@SequenceGenerator(name="iar_seq", schema = "eagle", sequenceName = "iar_seq", allocationSize = 1)
public class InvoiceArchive extends AuditableEagleEntity {

    public static final int URL_MAX_SIZE = 500;

    public static final String INV_MODE_ORGINAL = "O";

    public static final String DOCUMENT_SOURCE_SYSTEM_GRYF = "G";

    //FIELDS

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "iar_seq")
    private Long id;

    @Column(name = "SOURCE_PATH")
    private String sourcePath;

    @Column(name = "GEN_DATE")
    @Temporal(TemporalType.DATE)
    private Date genDate;

    @Column(name = "INV_ID")
    private Long invoiceId;

    @Convert(converter = BooleanConverter.class)
    @Column(name = "GENERATED")
    private Boolean generated;

    @Convert(converter = BooleanConverter.class)
    @Column(name = "CHECKED")
    private Boolean checked;

    @Column(name = "CHECK_DATE")
    @Temporal(TemporalType.DATE)
    private Date checkDate;

    @Column(name = "URL")
    private String url;

    @Column(name = "INV_MODE")
    private String invMode;

    @Convert(converter = BooleanConverter.class)
    @Column(name = "DUPLICATE")
    private Boolean duplicate;

    @Column(name = "DUPLICATE_DATE")
    @Temporal(TemporalType.DATE)
    private Date duplicateDate;

    @Column(name = "ATT_ID")
    private Long attributeId;

    @Column(name = "DOCUMENT_SOURCE_SYSTEM")
    private String documentSourceSystem;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public Date getGenDate() {
        return genDate;
    }

    public void setGenDate(Date genDate) {
        this.genDate = genDate;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Boolean isGenerated() {
        return generated;
    }

    public void setGenerated(Boolean generated) {
        this.generated = generated;
    }

    public Boolean isChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInvMode() {
        return invMode;
    }

    public void setInvMode(String invMode) {
        this.invMode = invMode;
    }

    public Boolean isDuplicate() {
        return duplicate;
    }

    public void setDuplicate(Boolean duplicate) {
        this.duplicate = duplicate;
    }

    public Date getDuplicateDate() {
        return duplicateDate;
    }

    public void setDuplicateDate(Date duplicateDate) {
        this.duplicateDate = duplicateDate;
    }

    public Long getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Long attributeId) {
        this.attributeId = attributeId;
    }

    public String getDocumentSourceSystem() {
        return documentSourceSystem;
    }

    public void setDocumentSourceSystem(String documentSourceSystem) {
        this.documentSourceSystem = documentSourceSystem;
    }
}
