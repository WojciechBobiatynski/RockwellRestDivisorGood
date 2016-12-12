package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.traininginstiutions;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingInstitutionSearchQueryDTO;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.traininginstiutions.TrainingInstitutionRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise.ID_ATTR_NAME;
import static pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution.*;

/**
 * Created by tomasz.bilski.ext on 2015-07-10.
 */
@Repository
public class TrainingInstitutionRepositoryImpl extends GenericRepositoryImpl<TrainingInstitution, Long> implements TrainingInstitutionRepository {

    @Override
    public List<TrainingInstitution> findByVatRegNum(String vatRegNum){
        TypedQuery<TrainingInstitution> query = entityManager.createNamedQuery(TrainingInstitution.FIND_BY_VAT_REG_NUM, TrainingInstitution.class);
        query.setParameter("vatRegNum", vatRegNum);
        return query.getResultList();
    }

    @Override
    public TrainingInstitution getForUpdate(Long id) {
        TypedQuery<TrainingInstitution> query = entityManager.createNamedQuery(TrainingInstitution.GET_FOR_UPDATE, TrainingInstitution.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public List<TrainingInstitution> findTrainingInstitutions(TrainingInstitutionSearchQueryDTO dto) {

        //FROM
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TrainingInstitution> cq = cb.createQuery(entityClass);
        Root<TrainingInstitution> from = cq.from(entityClass);

        //PREDICATE
        List<Predicate> predicatesList = new ArrayList<>();
        if(dto.getId() != null){
            predicatesList.add(cb.equal(from.get(ID_ATTR_NAME), dto.getId()));
        }
        if(!GryfStringUtils.isEmpty(dto.getName())){
            predicatesList.add(cb.like(cb.upper(from.<String>get(NAME_ATTR_NAME)), getLikeWildCard(dto.getName())));
        }
        if(!GryfStringUtils.isEmpty(dto.getVatRegNum())){
            predicatesList.add(cb.like(cb.upper(from.<String>get(VAT_REG_NUM_ATTR_NAME)), getLikeWildCard(dto.getVatRegNum())));
        }
        if(!GryfStringUtils.isEmpty(dto.getAddressInvoice())){
            predicatesList.add(cb.like(cb.upper(from.<String>get(ADDRESS_INVOICE_ATTR_NAME)), getLikeWildCard(dto.getAddressInvoice())));
        }
        if(!GryfStringUtils.isEmpty(dto.getZipCodeInvoiceCode())){
            predicatesList.add(cb.like(cb.upper(from.get(ZIP_CODE_INVOICE_ATTR_NAME).<String>get(ZipCode.ZIP_CODE_ATTR_NAME)), getLikeWildCard(dto.getZipCodeInvoiceCode())));
        }
        if(!GryfStringUtils.isEmpty(dto.getZipCodeInvoiceCity())){
            predicatesList.add(cb.like(cb.upper(from.get(ZIP_CODE_INVOICE_ATTR_NAME).<String>get(ZipCode.CITY_NAME_ATTR_NAME)), getLikeWildCard(dto.getZipCodeInvoiceCity())));
        }
        if(!GryfStringUtils.isEmpty(dto.getAddressCorr())){
            predicatesList.add(cb.like(cb.upper(from.<String>get(ADDRESS_CORR_ATTR_NAME)), getLikeWildCard(dto.getAddressCorr())));
        }
        if(!GryfStringUtils.isEmpty(dto.getZipCodeCorrCode())){
            predicatesList.add(cb.like(cb.upper(from.get(ZIP_CODE_CORR_ATTR_NAME).<String>get(ZipCode.ZIP_CODE_ATTR_NAME)), getLikeWildCard(dto.getZipCodeCorrCode())));
        }
        if(!GryfStringUtils.isEmpty(dto.getZipCodeCorrCity())){
            predicatesList.add(cb.like(cb.upper(from.get(ZIP_CODE_CORR_ATTR_NAME).<String>get(ZipCode.CITY_NAME_ATTR_NAME)), getLikeWildCard(dto.getZipCodeCorrCity())));
        }
        Predicate[] predicatesTab = predicatesList.toArray(new Predicate[predicatesList.size()]);

        //SELECT
        cq.select(from).where(cb.and(predicatesTab));

        //SORT
        sort(dto, cb, cq, from);

        //QUERY
        TypedQuery<TrainingInstitution> query = entityManager.createQuery(cq);

        //LIMIT
        limit(dto, query);

        return query.getResultList();
    }

    @Override
    public TrainingInstitution findTrainingInstitutionByUserLogin(String login) {
        TypedQuery<TrainingInstitution> query = entityManager.createQuery("select t from TrainingInstitution t join t.trainingInstitutionUsers u where u.login = :login", TrainingInstitution.class);
        query.setParameter("login", login);
        return query.getSingleResult();
    }

    @Override
    public TrainingInstitution findByExternalId(String externalId){
        TypedQuery<TrainingInstitution> query = entityManager.createNamedQuery("TrainingInstitution.findByExternalId", TrainingInstitution.class);
        query.setParameter("externalId", externalId);
        List<TrainingInstitution> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
}
