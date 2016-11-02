package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.individuals;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform.IndividualSearchQueryDTO;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.individuals.IndividualRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;

import javax.persistence.NoResultException;
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
    public Individual findByPesel(String pesel) {
        try {
            TypedQuery<Individual> query = entityManager.createNamedQuery(Individual.FIND_BY_PESEL, Individual.class);
            query.setParameter("pesel", pesel);
            return query.getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
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
        if (dto.getId() != null) {
            predicatesList.add(cb.equal(from.get(Individual.ID_ATTR_NAME), dto.getId()));
        }
        if (!GryfStringUtils.isEmpty(dto.getFirstName())) {
            predicatesList.add(cb.like(cb.upper(from.<String>get(Individual.FIRST_NAME_ATTR_NAME)), getLikeWildCard(dto.getFirstName())));
        }
        if (!GryfStringUtils.isEmpty(dto.getLastName())) {
            predicatesList.add(cb.like(cb.upper(from.<String>get(Individual.LAST_NAME_ATTR_NAME)), getLikeWildCard(dto.getLastName())));
        }
        if (!GryfStringUtils.isEmpty(dto.getPesel())) {
            predicatesList.add(cb.like(cb.upper(from.<String>get(Individual.PESEL_ATTR_NAME)), getLikeWildCard(dto.getPesel())));
        }
        if (!GryfStringUtils.isEmpty(dto.getDocumentNumber())) {
            predicatesList.add(cb.like(cb.upper(from.<String>get(Individual.DOCUMENT_NUMBER_ATTR_NAME)), getLikeWildCard(dto.getDocumentNumber())));
        }
        if (!GryfStringUtils.isEmpty(dto.getDocumentType())) {
            predicatesList.add(cb.like(cb.upper(from.<String>get(Individual.DOCUMENT_TYPE_ATTR_NAME)), getLikeWildCard(dto.getDocumentType())));
        }
        if (!GryfStringUtils.isEmpty(dto.getAddressInvoice())) {
            predicatesList.add(cb.like(cb.upper(from.<String>get(Individual.ADDRESS_INVOICE_ATTR_NAME)), getLikeWildCard(dto.getAddressInvoice())));
        }
        if (!GryfStringUtils.isEmpty(dto.getZipCodeInvoiceCode())) {
            predicatesList.add(cb.like(cb.upper(from.get(Individual.ZIP_CODE_INVOICE_ATTR_NAME).<String>get(ZipCode.ZIP_CODE_ATTR_NAME)), getLikeWildCard(dto.getZipCodeInvoiceCode())));
        }
        if (!GryfStringUtils.isEmpty(dto.getZipCodeInvoiceCity())) {
            predicatesList.add(cb.like(cb.upper(from.get(Individual.ZIP_CODE_INVOICE_ATTR_NAME).<String>get(ZipCode.CITY_NAME_ATTR_NAME)), getLikeWildCard(dto.getZipCodeInvoiceCity())));
        }
        if (!GryfStringUtils.isEmpty(dto.getAddressCorr())) {
            predicatesList.add(cb.like(cb.upper(from.<String>get(Individual.ADDRESS_CORR_ATTR_NAME)), getLikeWildCard(dto.getAddressCorr())));
        }
        if (!GryfStringUtils.isEmpty(dto.getZipCodeCorrCode())) {
            predicatesList.add(cb.like(cb.upper(from.get(Individual.ZIP_CODE_CORR_ATTR_NAME).<String>get(ZipCode.ZIP_CODE_ATTR_NAME)), getLikeWildCard(dto.getZipCodeCorrCode())));
        }
        if (!GryfStringUtils.isEmpty(dto.getZipCodeCorrCity())) {
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
