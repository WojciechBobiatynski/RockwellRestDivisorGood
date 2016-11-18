package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.IndividualContact;

/**
 * Created by adziobek on 18.11.2016.
 */
public interface IndividualContactRepository extends GenericRepository<IndividualContact, Long> {
    IndividualContact findByIndividualAndContactType(Long individualId, String contactTypeType);
}