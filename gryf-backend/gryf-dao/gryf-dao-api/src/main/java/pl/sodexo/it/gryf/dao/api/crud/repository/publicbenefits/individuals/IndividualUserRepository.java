package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.security.individuals.IndividualUser;

/**
 * Created by Isolution on 2017-02-13.
 */
public interface IndividualUserRepository extends GenericRepository<IndividualUser, Long> {

    IndividualUser findByPeselWithVerEmail(String pesel);

    IndividualUser findByIndividualPesel(String pesel);

    IndividualUser findByIndividualId(Long individualId);
}
