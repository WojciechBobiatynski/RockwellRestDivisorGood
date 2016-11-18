package pl.sodexo.it.gryf.model.publicbenefits.orders;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.GryfEntity;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Isolution on 2016-11-17.
 */
@ToString(exclude = {"grantProgram", "orderFlow"})
@Entity
@Table(name = "ORDER_FLOWS_FOR_GR_PROGRAMS", schema = "APP_PBE")
@NamedQueries({
        @NamedQuery(name = "OrderFlowForGrantProgram.findByGrantProgramInDate", query = "select distinct o from OrderFlowForGrantProgram o " +
                "where o.grantProgram.id = :grantProgramId " +
                "and (o.dateFrom is null or o.dateFrom <= :date) " +
                "and (o.dateTo is null or :date <= o.dateTo)")})
public class OrderFlowForGrantProgram extends GryfEntity {

    //FIELDS

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "GRANT_PROGRAM_ID", insertable = false, updatable = false)
    private GrantProgram grantProgram;

    @ManyToOne
    @JoinColumn(name = "ORDER_FLOW_ID", insertable = false, updatable = false)
    private OrderFlow orderFlow;

    @Column(name = "DATE_FROM")
    @Temporal(TemporalType.DATE)
    private Date dateFrom;

    @Column(name = "DATE_TO")
    @Temporal(TemporalType.DATE)
    private Date dateTo;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GrantProgram getGrantProgram() {
        return grantProgram;
    }

    public void setGrantProgram(GrantProgram grantProgram) {
        this.grantProgram = grantProgram;
    }

    public OrderFlow getOrderFlow() {
        return orderFlow;
    }

    public void setOrderFlow(OrderFlow orderFlow) {
        this.orderFlow = orderFlow;
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
        return Objects.equals(id, ((OrderFlowForGrantProgram) o).id);
    }
}