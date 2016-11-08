package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals;

import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform.IndividualSearchQueryDTO;
import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface IndividualRepository extends GenericRepository<Individual, Long> {

    Individual findByPesel(String pesel);

    Individual findById(Long id);

    Individual getForUpdate(Long id);

    List<Individual> findIndividuals(IndividualSearchQueryDTO dto);
}
