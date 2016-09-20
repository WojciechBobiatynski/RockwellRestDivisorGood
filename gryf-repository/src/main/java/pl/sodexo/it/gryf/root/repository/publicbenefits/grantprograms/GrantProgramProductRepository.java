package pl.sodexo.it.gryf.root.repository.publicbenefits.grantprograms;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramProduct;
import pl.sodexo.it.gryf.root.repository.GenericRepository;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Repository
public class GrantProgramProductRepository extends GenericRepository<GrantProgramProduct, Long> {

    public List<GrantProgramProduct> findByGrantProgramInDate(Long grantProgramId, Date date){
        TypedQuery<GrantProgramProduct> query = entityManager.createNamedQuery(GrantProgramProduct.FIND_BY_GRANT_PROGRAM_IN_DATE, GrantProgramProduct.class);
        query.setParameter("grantProgramId", grantProgramId);
        query.setParameter("date", date);
        return query.getResultList();
    }



}
