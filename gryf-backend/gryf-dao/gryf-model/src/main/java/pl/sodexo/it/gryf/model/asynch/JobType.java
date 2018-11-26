package pl.sodexo.it.gryf.model.asynch;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

import static pl.sodexo.it.gryf.model.asynch.JobType.*;

/**
 * Created by Isolution on 2016-12-01.
 */
@ToString
@Entity
@Table(name = "JOB_TYPE", schema = "APP_PBE")
@NamedQueries({
        @NamedQuery(name = QUERY_FIND_BY_GRANT_PROGRAM_ID_AND_NAME, query="select iType from JobType iType " +
                "where iType.grantProgramId = :grantProgramId " +
                " and  iType.name = :name"),
        @NamedQuery(name = QUERY_FIND_BY_NAME, query="select iType from JobType iType " +
                "where iType.name = :name"),
        @NamedQuery(name = QUERY_FIND_BY_GRANT_PROGRAM_ID, query="select iType from JobType iType " +
                "where iType.grantProgramId = :grantProgramId ")
})
@SequenceGenerator(name = IMPORT_TYPE_SEQ, schema = "eagle", sequenceName = IMPORT_TYPE_SEQ, allocationSize = 1)
public class JobType {

    public final static String IMPORT_TYPE_SEQ = "job_type_seq";

    //Queries Names
    public final static String QUERY_FIND_BY_GRANT_PROGRAM_ID_AND_NAME = "JobType.findByGrantProgramIdAndName";
    public final static String PARAM_GRANT_PROGRAM_ID = "grantProgramId";
    public static final String PARAM_NAME = "name";
    public static final String QUERY_FIND_BY_NAME = "JobType.findByName";
    public static final String QUERY_FIND_BY_GRANT_PROGRAM_ID = "JobType.findByGrantProgramId";

    @Getter
    @Setter
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = IMPORT_TYPE_SEQ)
    private Long id;

    @Getter
    @Setter
    @Column(name = "GRANT_PROGRAM_ID")
    private Long grantProgramId;

    @Getter
    @Setter
    @Column(name = "LABEL")
    private String label;

    @Getter
    @Setter
    @Column(name = "NAME")
    private String name;

    @Getter
    @Setter
    @Column(name = "JOB_NAME")
    private String jobName;

    @Getter
    @Setter
    @Column(name = "SERVICE_NAME")
    private String serviceNname;


}
