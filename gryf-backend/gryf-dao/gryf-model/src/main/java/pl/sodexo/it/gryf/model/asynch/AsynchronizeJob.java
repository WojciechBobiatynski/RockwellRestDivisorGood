package pl.sodexo.it.gryf.model.asynch;

import lombok.ToString;
import pl.sodexo.it.gryf.model.api.VersionableEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Isolution on 2016-12-01.
 */
@ToString
@Entity
@Table(name = "ASYNCH_JOBS", schema = "APP_PBE")
@NamedQueries({
        @NamedQuery(name = "AsynchronizeJob.findFirstAsynchronizeJobToWork", query="select o from AsynchronizeJob o " +
                "where o.status = :status and (o.nextStartTimestamp is null or CURRENT_DATE <= o.nextStartTimestamp) " +
                "order by o.createdTimestamp ")
})
@SequenceGenerator(name = "asyn_job_seq", schema = "eagle", sequenceName = "asyn_job_seq", allocationSize = 1)
public class AsynchronizeJob extends VersionableEntity {

    //STATIC FIELDS

    public static final int DESCRIPTION_MAX_SIZE = 1000;

    //PRIVATE FIELDS

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "asyn_job_seq")
    private Long id;

   // @Enumerated(EnumType.STRING) //Toddo
    @Column(name = "TYPE")
    private String type;

    @Column(name = "PARAMS")
    private String params;

    @Column(name = "DESCRIPTION")
    private String  description;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private AsynchronizeJobStatus status;

    @Column(name = "NEXT_START_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date nextStartTimestamp;

    @Column(name = "START_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTimestamp;

    @Column(name = "STOP_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date stopTimestamp;

    @Column(name = "ORDER_ID")
    private Long orderId;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AsynchronizeJobStatus getStatus() {
        return status;
    }

    public void setStatus(AsynchronizeJobStatus status) {
        this.status = status;
    }

    public Date getNextStartTimestamp() {
        return nextStartTimestamp;
    }

    public void setNextStartTimestamp(Date nextStartTimestamp) {
        this.nextStartTimestamp = nextStartTimestamp;
    }

    public Date getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(Date startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public Date getStopTimestamp() {
        return stopTimestamp;
    }

    public void setStopTimestamp(Date stopTimestamp) {
        this.stopTimestamp = stopTimestamp;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
