package pl.sodexo.it.gryf.model.stock.products;

import lombok.ToString;
import pl.sodexo.it.gryf.model.GryfEntity;
import pl.sodexo.it.gryf.model.publicbenefits.reimbursement.ReimbursementTraining;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by tomasz.bilski.ext on 2015-09-15.
 */
@ToString(exclude = "reimbursementTraining")
@Entity
@Table(name = "REMITTANCE_PRODUCT_INSTANCES", schema = "APP_RMB")
public class RemittanceProductInstances extends GryfEntity{

    //FIELDS

    @Id
    @Column(name = "ROWID")
    private String rowid;

    @Column(name = "SCAN_NUMBER")
    private String scanNumber;

    @Column(name = "NUM")
    private String num;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "CRC")
    private String crc;

    @Column(name = "PRE_COUNTRY_ERR")
    private String preCountryErr;

    @Column(name = "PRE_PRODUCT_ERR")
    private String preProductErr;

    @Column(name = "PRE_OVERDUE_ERR")
    private String preOverdueErr;

    @Column(name = "PRE_VALUE_ERR")
    private String preValueErr;

    @Column(name = "PRE_CRC_ERR")
    private String preCrcErr;

    @Column(name = "ERR_COUNTRY")
    private String errCountry;

    @Column(name = "ERR_UNIDENTIFIED")
    private String errUnidentified;

    @Column(name = "ERR_CRC")
    private String errCrc;

    @Column(name = "ERR_STOLEN")
    private String errStolen;

    @Column(name = "ERR_RETURNED")
    private String errReturned;

    @Column(name = "ERR_ALREADY_READ")
    private String errAlreadyRead;

    @Column(name = "ERR_VOID")
    private String errVoid;

    @Column(name = "ERR_NEVER_EMITED")
    private String errNeverEmited;

    @Column(name = "ERR_OVERDUE")
    private String errOverdue;

    @Column(name = "ERR_VALUE")
    private String errValue;

    @Column(name = "ERR_PRODUCT")
    private String errProduct;

    @Column(name = "ERR_DESTROYED")
    private String errDestroyed;

    @Column(name = "ERR_CANCELLED")
    private String errCancelled;

    @Column(name = "ERR_NOT_RELEASED")
    private String errNotReleased;

    @Column(name = "ERR_NOT_IN_CONTRACT")
    private String errNotInContract;

    @Column(name = "AUTO_CROSS_ACCEPTED")
    private String autoCrossAccepted;

    @Column(name = "USER_ACCEPTED")
    private String userAccepted;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "MODIFIED")
    private String modified;

    @Column(name = "ERR_PAP")
    private String errPap;

    @Column(name = "E_TRANSFERED")
    private String eTransfered;

    @Column(name = "LINE_NO")
    private Integer lineNo;

    @Column(name = "ENT_TYPE")
    private String entType;

    @Column(name = "ENT_VALUE")
    private BigDecimal entValue;

    @Column(name = "ENT_EXPIRED_DATE")
    private Timestamp entExpiredDate;

    @Column(name = "WITHDRAWED")
    private String withdrawed;

    @ManyToOne
    @JoinColumn(name = "REIMBURSEMENT_TRAINING_ID", referencedColumnName = "ID")
    private ReimbursementTraining reimbursementTraining;

    //GETTERS & SETTERS

    public String getRowid() {
        return rowid;
    }

    public void setRowid(String rowid) {
        this.rowid = rowid;
    }

    public String getScanNumber() {
        return scanNumber;
    }

    public void setScanNumber(String scanNumber) {
        this.scanNumber = scanNumber;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = crc;
    }

    public String getPreCountryErr() {
        return preCountryErr;
    }

    public void setPreCountryErr(String preCountryErr) {
        this.preCountryErr = preCountryErr;
    }

    public String getPreProductErr() {
        return preProductErr;
    }

    public void setPreProductErr(String preProductErr) {
        this.preProductErr = preProductErr;
    }

    public String getPreOverdueErr() {
        return preOverdueErr;
    }

    public void setPreOverdueErr(String preOverdueErr) {
        this.preOverdueErr = preOverdueErr;
    }

    public String getPreValueErr() {
        return preValueErr;
    }

    public void setPreValueErr(String preValueErr) {
        this.preValueErr = preValueErr;
    }

    public String getPreCrcErr() {
        return preCrcErr;
    }

    public void setPreCrcErr(String preCrcErr) {
        this.preCrcErr = preCrcErr;
    }

    public String getErrCountry() {
        return errCountry;
    }

    public void setErrCountry(String errCountry) {
        this.errCountry = errCountry;
    }

    public String getErrUnidentified() {
        return errUnidentified;
    }

    public void setErrUnidentified(String errUnidentified) {
        this.errUnidentified = errUnidentified;
    }

    public String getErrCrc() {
        return errCrc;
    }

    public void setErrCrc(String errCrc) {
        this.errCrc = errCrc;
    }

    public String getErrStolen() {
        return errStolen;
    }

    public void setErrStolen(String errStolen) {
        this.errStolen = errStolen;
    }

    public String getErrReturned() {
        return errReturned;
    }

    public void setErrReturned(String errReturned) {
        this.errReturned = errReturned;
    }

    public String getErrAlreadyRead() {
        return errAlreadyRead;
    }

    public void setErrAlreadyRead(String errAlreadyRead) {
        this.errAlreadyRead = errAlreadyRead;
    }

    public String getErrVoid() {
        return errVoid;
    }

    public void setErrVoid(String errVoid) {
        this.errVoid = errVoid;
    }

    public String getErrNeverEmited() {
        return errNeverEmited;
    }

    public void setErrNeverEmited(String errNeverEmited) {
        this.errNeverEmited = errNeverEmited;
    }

    public String getErrOverdue() {
        return errOverdue;
    }

    public void setErrOverdue(String errOverdue) {
        this.errOverdue = errOverdue;
    }

    public String getErrValue() {
        return errValue;
    }

    public void setErrValue(String errValue) {
        this.errValue = errValue;
    }

    public String getErrProduct() {
        return errProduct;
    }

    public void setErrProduct(String errProduct) {
        this.errProduct = errProduct;
    }

    public String getErrDestroyed() {
        return errDestroyed;
    }

    public void setErrDestroyed(String errDestroyed) {
        this.errDestroyed = errDestroyed;
    }

    public String getErrCancelled() {
        return errCancelled;
    }

    public void setErrCancelled(String errCancelled) {
        this.errCancelled = errCancelled;
    }

    public String getErrNotReleased() {
        return errNotReleased;
    }

    public void setErrNotReleased(String errNotReleased) {
        this.errNotReleased = errNotReleased;
    }

    public String getErrNotInContract() {
        return errNotInContract;
    }

    public void setErrNotInContract(String errNotInContract) {
        this.errNotInContract = errNotInContract;
    }

    public String getAutoCrossAccepted() {
        return autoCrossAccepted;
    }

    public void setAutoCrossAccepted(String autoCrossAccepted) {
        this.autoCrossAccepted = autoCrossAccepted;
    }

    public String getUserAccepted() {
        return userAccepted;
    }

    public void setUserAccepted(String userAccepted) {
        this.userAccepted = userAccepted;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getErrPap() {
        return errPap;
    }

    public void setErrPap(String errPap) {
        this.errPap = errPap;
    }

    public String geteTransfered() {
        return eTransfered;
    }

    public void seteTransfered(String eTransfered) {
        this.eTransfered = eTransfered;
    }

    public Integer getLineNo() {
        return lineNo;
    }

    public void setLineNo(Integer lineNo) {
        this.lineNo = lineNo;
    }

    public String getEntType() {
        return entType;
    }

    public void setEntType(String entType) {
        this.entType = entType;
    }

    public BigDecimal getEntValue() {
        return entValue;
    }

    public void setEntValue(BigDecimal entValue) {
        this.entValue = entValue;
    }

    public Timestamp getEntExpiredDate() {
        return entExpiredDate;
    }

    public void setEntExpiredDate(Timestamp entExpiredDate) {
        this.entExpiredDate = entExpiredDate;
    }

    public String getWithdrawed() {
        return withdrawed;
    }

    public void setWithdrawed(String withdrawed) {
        this.withdrawed = withdrawed;
    }

    public ReimbursementTraining getReimbursementTraining() {
        return reimbursementTraining;
    }

    public void setReimbursementTraining(ReimbursementTraining reimbursementTraining) {
        this.reimbursementTraining = reimbursementTraining;
    }
}
