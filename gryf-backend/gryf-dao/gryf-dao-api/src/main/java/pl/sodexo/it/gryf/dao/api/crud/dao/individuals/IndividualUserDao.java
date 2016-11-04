package pl.sodexo.it.gryf.dao.api.crud.dao.individuals;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.model.security.individuals.IndividualUser;

/**
 * Dao dla uzytkownika osoby fizycznej
 *
 * Created by akmiecinski on 19.10.2016.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface IndividualUserDao extends JpaRepository<IndividualUser, Long> {

    @Query("SELECT usr FROM IndividualUser usr "
            + "JOIN Individual ind on ind = usr.individual "
            + "JOIN IndividualContact cont ON cont.individual = ind "
            + "WHERE cont.contactType.type = 'VER_EMAIL' AND cont.contactData = :email AND ind.pesel = :pesel")
    IndividualUser findByPeselAndEmail(@Param("pesel") String pesel, @Param("email") String email);

    @Query("SELECT usr FROM IndividualUser usr "
            + "JOIN Individual ind on ind = usr.individual "
            + "JOIN IndividualContact cont ON cont.individual = ind "
            + "WHERE cont.contactType.type = 'VER_EMAIL' AND ind.pesel = :pesel")
    IndividualUser findByPesel(@Param("pesel") String pesel);

    IndividualUser findByIndividual_Id(Long id);
}
