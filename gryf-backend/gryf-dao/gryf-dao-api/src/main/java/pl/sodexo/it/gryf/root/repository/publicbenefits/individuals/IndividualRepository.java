package pl.sodexo.it.gryf.root.repository.publicbenefits.individuals;

import pl.sodexo.it.gryf.dto.publicbenefits.individuals.searchform.IndividualSearchQueryDTO;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.root.repository.GenericRepository;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface IndividualRepository extends GenericRepository<Individual, Long> {

    List<Individual> findByPesel(String pesel);

    Individual getForUpdate(Long id);

    List<Individual> findIndividuals(IndividualSearchQueryDTO dto);
}
