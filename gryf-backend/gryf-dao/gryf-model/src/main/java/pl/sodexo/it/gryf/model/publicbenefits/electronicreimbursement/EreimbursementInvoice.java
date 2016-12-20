package pl.sodexo.it.gryf.model.publicbenefits.electronicreimbursement;

import pl.sodexo.it.gryf.model.api.VersionableEntity;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderInvoice;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Isolution on 2016-12-20.
 */
@Entity
@Table(name = "E_REIMBURSEMENT_INVOICES", schema = "APP_PBE")
public class EreimbursementInvoice extends VersionableEntity {

    @Id
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator = "pk_seq")
    private Long id;

    @JoinColumn(name = "E_REIMBURSEMENT_ID", referencedColumnName = "ID")
    @ManyToOne
    private Ereimbursement ereimbursement;

    @Column(name = "INVOICE_ID")
    private Long invoiceId;

    @Column(name = "INVOICE_NUMBER")
    private String invoiceNumber;

    @Column(name = "INVOICE_TYPE")
    private String invoiceType;

    @Column(name = "INVOICE_DATE")
    @Temporal(TemporalType.DATE)
    private Date invoiceDate;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ereimbursement getEreimbursement() {
        return ereimbursement;
    }

    public void setEreimbursement(Ereimbursement ereimbursement) {
        this.ereimbursement = ereimbursement;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
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
        return Objects.equals(id, ((EreimbursementInvoice) o).id);
    }
}
