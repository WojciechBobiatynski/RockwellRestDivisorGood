package pl.sodexo.it.gryf.model.publicbenefits.orders;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.GryfEntity;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationVersion;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Created by tomasz.bilski.ext on 2015-08-19.
 */
@ToString(exclude = {"grantApplicationVersion", "orderFlow"})
@Entity
@Table(name = "ORDER_FLOWS_FOR_GAPP_VERSIONS", schema = "APP_PBE")
@NamedQueries({
        @NamedQuery(name = "OrderFlowForGrantApplicationVersion.findByGrantApplicationVersionInDate",
                query = "select distinct offgav from OrderFlowForGrantApplicationVersion offgav " +
                "where offgav.grantApplicationVersion.id = :versionId " +
                "and (offgav.dateFrom is null or offgav.dateFrom <= :date) " +
                "and (offgav.dateTo is null or :date <= offgav.dateTo)")})
public class OrderFlowForGrantApplicationVersion extends GryfEntity{

    //FIELDS

    @Id
    private Long id;

    @Column(name = "DATE_FROM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;

    @Column(name = "DATE_TO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;

    @ManyToOne
    @JoinColumn(name = "VERSION_ID", insertable=false, updatable=false)
    private GrantApplicationVersion grantApplicationVersion;

    @ManyToOne
    @JoinColumn(name = "ORDER_FLOW_ID", insertable=false, updatable=false)
    private OrderFlow orderFlow;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public GrantApplicationVersion getGrantApplicationVersion() {
        return grantApplicationVersion;
    }

    public void setGrantApplicationVersion(GrantApplicationVersion grantApplicationVersion) {
        this.grantApplicationVersion = grantApplicationVersion;
    }

    public OrderFlow getOrderFlow() {
        return orderFlow;
    }

    public void setOrderFlow(OrderFlow orderFlow) {
        this.orderFlow = orderFlow;
    }

    //PUBLIC METHODS - HASH CODE & EQUALS

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
        return Objects.equals(id, ((OrderFlowForGrantApplicationVersion) o).id);
    }
}
