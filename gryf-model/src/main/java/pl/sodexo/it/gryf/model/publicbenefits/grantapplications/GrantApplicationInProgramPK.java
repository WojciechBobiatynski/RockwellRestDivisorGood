package pl.sodexo.it.gryf.model.publicbenefits.grantapplications;

import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author tomasz.bilski.ext
 */
@ToString
@Embeddable
public class GrantApplicationInProgramPK implements Serializable {

    //FIELDS

    @Column(name = "GRANT_PROGRAM_ID")
    private Long programId;

    @Column(name = "VERSION_ID")
    private Long versionId;

    //CONSTRUCTORS

    public GrantApplicationInProgramPK() {
    }

    public GrantApplicationInProgramPK(Long programId, Long versionId) {
        this.programId = programId;
        this.versionId = versionId;
    }

    //GETTERS & SETTERS

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
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
        return Objects.equals(programId, ((GrantApplicationInProgramPK) o).programId) &&
                Objects.equals(versionId, ((GrantApplicationInProgramPK) o).versionId);
    }

    @Override
    public int hashCode() {
        return (Objects.hashCode(programId) % 102 +
                Objects.hashCode(versionId)) % 102;
    }

    
}
