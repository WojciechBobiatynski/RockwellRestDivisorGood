package pl.sodexo.it.gryf.root.repository.publicbenefits.grantprograms;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.root.repository.GenericRepository;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Repository
public class GrantProgramRepository extends GenericRepository<GrantProgram, Long> {

    public List<GrantProgram> findProgramsByDate(Date date){
        TypedQuery<GrantProgram> query = entityManager.createNamedQuery(GrantProgram.FIND_BY_DATE, GrantProgram.class);
        query.setParameter("date", date);
        return query.getResultList();
    }

}
