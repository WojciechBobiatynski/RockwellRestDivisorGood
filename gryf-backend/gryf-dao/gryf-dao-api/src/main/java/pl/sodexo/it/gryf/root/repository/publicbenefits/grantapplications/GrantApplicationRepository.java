package pl.sodexo.it.gryf.root.repository.publicbenefits.grantapplications;

import pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.searchform.GrantApplicationSearchQueryDTO;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.root.repository.GenericRepository;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface GrantApplicationRepository extends GenericRepository<GrantApplication, Long> {

    List<GrantApplication> findApplications(GrantApplicationSearchQueryDTO searchDTO);
}