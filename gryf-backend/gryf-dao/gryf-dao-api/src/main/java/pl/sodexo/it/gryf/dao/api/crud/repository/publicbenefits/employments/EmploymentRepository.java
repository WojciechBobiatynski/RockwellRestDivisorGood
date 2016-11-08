package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.employments;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.employment.Employment;

/**
 * Created by adziobek on 08.11.2016.
 */
public interface EmploymentRepository extends GenericRepository<Employment, Long> {

    Employment findByIndividualIdAndEnterpriseId(Long individualId, Long enterpriseId);
}