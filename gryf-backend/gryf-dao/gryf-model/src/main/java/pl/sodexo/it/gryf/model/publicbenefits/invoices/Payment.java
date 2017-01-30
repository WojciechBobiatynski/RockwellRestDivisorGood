package pl.sodexo.it.gryf.model.publicbenefits.invoices;

import com.fasterxml.jackson.annotation.JsonBackReference;
import pl.sodexo.it.gryf.model.api.AuditableEntity;
import pl.sodexo.it.gryf.model.api.BooleanConverter;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Isolution on 2017-01-30.
 */
@Entity
@Table(name = "NAV_MC_TRANSFERS_PRC", schema = "APP_FIN")
@NamedQueries({
        @NamedQuery(name = "Payment.findByOrder", query = "select pi from Payment pi " +
                "where pi.order.id = :orderId order by pi.paymentDate ")})
public class Payment extends AuditableEntity{

    @Id
    @Column(name = "ID")
    private Long id;

    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ID")
    @ManyToOne
    private Order order;

    @Convert(converter = BooleanConverter.class)
    @Column(name = "USED")
    private Boolean used;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "TRANSFER_AMOUNT")
    private BigDecimal transferAmount;

    @Column(name = "MATCH_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date matchDate;

    @Column(name = "PAYMENT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;

    @Column(name = "TRANSFER_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transferDate;

    @Column(name = "TRANSFER_DETAIL")
    private String transferDetail;

    //GETTERS / SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public Date getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(Date matchDate) {
        this.matchDate = matchDate;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    public String getTransferDetail() {
        return transferDetail;
    }

    public void setTransferDetail(String transferDetail) {
        this.transferDetail = transferDetail;
    }
}
