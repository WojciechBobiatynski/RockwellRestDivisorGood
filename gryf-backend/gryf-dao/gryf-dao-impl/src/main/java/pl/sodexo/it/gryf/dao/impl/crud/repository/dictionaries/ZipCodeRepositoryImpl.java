package pl.sodexo.it.gryf.dao.impl.crud.repository.dictionaries;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.common.dto.zipcodes.searchform.ZipCodeSearchQueryDTO;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.dictionaries.ZipCodeRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.dictionaries.State;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-06-15.
 */
@Repository
public class ZipCodeRepositoryImpl extends GenericRepositoryImpl<ZipCode, Long> implements ZipCodeRepository {

    @Override
    public List<ZipCode> findZipCodes(ZipCodeSearchQueryDTO zipCode){

        //FROM
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ZipCode> cg = cb.createQuery(ZipCode.class);
        Root<ZipCode> from = cg.from(ZipCode.class);

        //PREDICATE
        List<Predicate> predicatesList = new ArrayList<>();
        if(!GryfStringUtils.isEmpty(zipCode.getZipCode())){
            predicatesList.add(cb.like(cb.upper(from.<String>get(ZipCode.ZIP_CODE_ATTR_NAME)), getLikeWildCard(zipCode.getZipCode())));
        }
        if (!GryfStringUtils.isEmpty(zipCode.getCityName())){
            predicatesList.add(cb.like(cb.upper(from.<String>get(ZipCode.CITY_NAME_ATTR_NAME)), getLikeWildCard(zipCode.getCityName().toUpperCase())));
        }
        if(zipCode.getActive() != null){
            predicatesList.add(cb.equal(from.get(ZipCode.ACTIVE_ATTR_NAME), zipCode.getActive()));
        }
        if(zipCode.getStateId() != null){
            predicatesList.add(cb.equal(from.get(ZipCode.STATE_ATTR_NAME).get(State.ID_ATTR_NAME), zipCode.getStateId()));
        }
        Predicate[] predicatesTab = predicatesList.toArray(new Predicate[predicatesList.size()]);

        //SELECT
        cg.select(from).where(cb.and(predicatesTab));

        //SORT
        sort(zipCode, cb, cg, from);

        //QUERY
        TypedQuery<ZipCode> query = entityManager.createQuery(cg);

        //LIMIT
        limit(zipCode, query);

        return query.getResultList();
    }

}
