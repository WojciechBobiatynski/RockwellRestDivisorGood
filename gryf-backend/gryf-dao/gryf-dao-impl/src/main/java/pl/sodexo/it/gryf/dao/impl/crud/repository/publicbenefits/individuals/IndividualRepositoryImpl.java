package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.individuals;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform.IndividualSearchQueryDTO;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualRepository;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IndividualRepositoryImpl extends GenericRepositoryImpl<Individual, Long> implements IndividualRepository {

    @Override
    public List<Individual> findByPesel(String pesel){
        TypedQuery<Individual> query = entityManager.createNamedQuery(Individual.FIND_BY_PESEL, Individual.class);
        query.setParameter("pesel", pesel);
        return query.getResultList();
    }

    @Override
    public Individual getForUpdate(Long id) {
        TypedQuery<Individual> query = entityManager.createNamedQuery(Individual.GET_FOR_UPDATE, Individual.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public List<Individual> findIndividuals(IndividualSearchQueryDTO dto) {

        //FROM
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Individual> cq = cb.createQuery(entityClass);
        Root<Individual> from = cq.from(entityClass);

        //PREDICATE
        List<Predicate> predicatesList = new ArrayList<>();
        if(dto.getId() != null){
            predicatesList.add(cb.equal(from.get(Individual.ID_ATTR_NAME), dto.getId()));
        }
        if(!StringUtils.isEmpty(dto.getFirstName())){
            predicatesList.add(cb.like(cb.upper(from.<String>get(Individual.FIRST_NAME_ATTR_NAME)), getLikeWildCard(dto.getFirstName())));
        }
        if(!StringUtils.isEmpty(dto.getLastName())){
            predicatesList.add(cb.like(cb.upper(from.<String>get(Individual.LAST_NAME_ATTR_NAME)), getLikeWildCard(dto.getLastName())));
        }
        if(!StringUtils.isEmpty(dto.getPesel())){
            predicatesList.add(cb.like(cb.upper(from.<String>get(Individual.PESEL_ATTR_NAME)), getLikeWildCard(dto.getPesel())));
        }
        if(!StringUtils.isEmpty(dto.getDocumentNumber())){
            predicatesList.add(cb.like(cb.upper(from.<String>get(Individual.DOCUMENT_NUMBER_ATTR_NAME)), getLikeWildCard(dto.getDocumentNumber())));
        }
        if(!StringUtils.isEmpty(dto.getDocumentType())){
            predicatesList.add(cb.like(cb.upper(from.<String>get(Individual.DOCUMENT_TYPE_ATTR_NAME)), getLikeWildCard(dto.getDocumentType())));
        }
        if(!StringUtils.isEmpty(dto.getAddressInvoice())){
            predicatesList.add(cb.like(cb.upper(from.<String>get(Individual.ADDRESS_INVOICE_ATTR_NAME)), getLikeWildCard(dto.getAddressInvoice())));
        }
        if(!StringUtils.isEmpty(dto.getZipCodeInvoiceCode())){
            predicatesList.add(cb.like(cb.upper(from.get(Individual.ZIP_CODE_INVOICE_ATTR_NAME).<String>get(ZipCode.ZIP_CODE_ATTR_NAME)), getLikeWildCard(dto.getZipCodeInvoiceCode())));
        }
        if(!StringUtils.isEmpty(dto.getZipCodeInvoiceCity())){
            predicatesList.add(cb.like(cb.upper(from.get(Individual.ZIP_CODE_INVOICE_ATTR_NAME).<String>get(ZipCode.CITY_NAME_ATTR_NAME)), getLikeWildCard(dto.getZipCodeInvoiceCity())));
        }
        if(!StringUtils.isEmpty(dto.getAddressCorr())){
            predicatesList.add(cb.like(cb.upper(from.<String>get(Individual.ADDRESS_CORR_ATTR_NAME)), getLikeWildCard(dto.getAddressCorr())));
        }
        if(!StringUtils.isEmpty(dto.getZipCodeCorrCode())){
            predicatesList.add(cb.like(cb.upper(from.get(Individual.ZIP_CODE_CORR_ATTR_NAME).<String>get(ZipCode.ZIP_CODE_ATTR_NAME)), getLikeWildCard(dto.getZipCodeCorrCode())));
        }
        if(!StringUtils.isEmpty(dto.getZipCodeCorrCity())){
            predicatesList.add(cb.like(cb.upper(from.get(Individual.ZIP_CODE_CORR_ATTR_NAME).<String>get(ZipCode.CITY_NAME_ATTR_NAME)), getLikeWildCard(dto.getZipCodeCorrCity())));
        }
        Predicate[] predicatesTab = predicatesList.toArray(new Predicate[predicatesList.size()]);

        //SELECT
        cq.select(from).where(cb.and(predicatesTab));

        //SORT
        sort(dto, cb, cq, from);

        //QUERY
        TypedQuery<Individual> query = entityManager.createQuery(cq);

        //LIMIT
        limit(dto, query);

        return query.getResultList();
    }

}