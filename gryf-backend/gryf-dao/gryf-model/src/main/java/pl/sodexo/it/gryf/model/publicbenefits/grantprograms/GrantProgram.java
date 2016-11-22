package pl.sodexo.it.gryf.model.publicbenefits.grantprograms;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.ToString;
import pl.sodexo.it.gryf.model.api.DictionaryEntity;
import pl.sodexo.it.gryf.model.api.GryfEntity;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationInProgram;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;

import javax.persistence.*;
import java.util.*;

/**
 *
 * @author tomasz.bilski.ext
 */
@ToString(exclude = {"grantOwnerId", "programParams", "applicationInPrograms"})
@Entity
@Table(name = "GRANT_PROGRAMS", schema = "APP_PBE")
@NamedQueries({
        @NamedQuery(name = GrantProgram.FIND_BY_DATE, query = "select e from GrantProgram e " +
                                                                "where  (e.startDate is null or e.startDate <= :date) " +
                                                                "and (e.endDate is null or :date <= e.endDate) ")})
public class GrantProgram extends GryfEntity implements DictionaryEntity {

    //STATIC FIELDS - NAMED QUERY

    public static final String FIND_BY_DATE = "GrantProgram.findByDate";

    //STATIC FIELDS - ATRIBUTES

    public static final String ID_ATTR_NAME = "id";
    public static final String PROGRAM_NAME_ATTR_NAME = "programName";
    public static final String PROGRAM_OWNER_ATTR_NAME = "grantOwnerId";

    //FIELDS

    @Id
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "GRANT_OWNER_ID", referencedColumnName = "ID")
    private GrantOwner grantOwnerId;

    @Column(name = "PROGRAM_NAME")
    private String programName;

    @Column(name = "START_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @JsonManagedReference("programParams")
    @OneToMany(mappedBy = "grantProgram")
    private List<GrantProgramParam> programParams;

    @JsonManagedReference("applicationInPrograms")
    @OneToMany(mappedBy = "grantProgram")
    private List<GrantApplicationInProgram> applicationInPrograms;

    @JsonManagedReference("orders")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "grantProgram")
    private List<Order> orders;

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GrantOwner getGrantOwner() {
        return grantOwnerId;
    }

    public void setGrantOwner(GrantOwner grantOwnerId) {
        this.grantOwnerId = grantOwnerId;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    //LIST METHODS

    private List<GrantApplicationInProgram> getInitializedApplicationInPrograms() {
        if (applicationInPrograms == null)
            applicationInPrograms = new ArrayList<>();
        return applicationInPrograms;
    }

    public List<GrantApplicationInProgram> getApplicationInPrograms() {
        return Collections.unmodifiableList(getInitializedApplicationInPrograms());
    }

    private List<GrantProgramParam> getInitializedProgramParams() {
        if (programParams == null)
            programParams = new ArrayList<>();
        return programParams;
    }

    public List<GrantProgramParam> getProgramParams() {
        return Collections.unmodifiableList(getInitializedProgramParams());
    }

    //DICTIONARY METHODS

    public Object getDictionaryId() {
        return id;
    }

    public String getDictionaryName() {
        return programName;
    }

    public String getOrderField(){
        return PROGRAM_NAME_ATTR_NAME;
    }

    public String getActiveField() {
        return null;
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
        return Objects.equals(id, ((GrantProgram) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

    public GrantProgram() {
    }

}
