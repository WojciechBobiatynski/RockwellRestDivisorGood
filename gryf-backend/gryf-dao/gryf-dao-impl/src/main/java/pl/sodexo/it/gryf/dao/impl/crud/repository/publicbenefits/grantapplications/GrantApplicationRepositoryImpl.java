package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.grantapplications;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.searchform.GrantApplicationSearchQueryDTO;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.grantapplications.GrantApplicationRepository;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication.*;
import static pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationBasicData.*;
/**
 * Created by tomasz.bilski.ext on 2015-06-30.
 */
@Repository
public class GrantApplicationRepositoryImpl extends GenericRepositoryImpl<GrantApplication, Long> implements GrantApplicationRepository {

    @Override
    public List<GrantApplication> findApplications(GrantApplicationSearchQueryDTO searchDTO){
        //FROM
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<GrantApplication> cq = cb.createQuery(GrantApplication.class);
        Root<GrantApplication> from = cq.from(GrantApplication.class);

        //PREDICATE
        List<Predicate> predicatesList = new ArrayList<>();
        if(searchDTO.getId() != null){
            predicatesList.add(cb.equal(from.get(ID_ATTR_NAME), searchDTO.getId()));
        }
        if(searchDTO.getStatusId() != null){
            predicatesList.add(cb.equal(from.get(STATUS_ATTR_NAME).get(ID_ATTR_NAME), searchDTO.getStatusId()));
        }
        if(searchDTO.getEnterpriseId() != null){
            predicatesList.add(cb.equal(from.get(ENTERPRISE_ATTR_NAME).get(ID_ATTR_NAME), searchDTO.getEnterpriseId()));
        }
        if(!StringUtils.isEmpty(searchDTO.getEnterpriseName())){
            predicatesList.add(cb.like(cb.upper(from.get(BASIC_DATA_ATTR_NAME).<String>get(ENTERPRISE_NAME_ATTR_NAME)), getLikeWildCard(searchDTO.getEnterpriseName())));
        }
        if(!StringUtils.isEmpty(searchDTO.getVatRegNum())){
            predicatesList.add(cb.like(cb.upper(from.<String>get(BASIC_DATA_ATTR_NAME).<String>get(VAT_REG_NUM_ATTR_NAME)), getLikeWildCard(searchDTO.getVatRegNum())));
        }
        if(!StringUtils.isEmpty(searchDTO.getAddressInvoice())){
            predicatesList.add(cb.like(cb.upper(from.<String>get(BASIC_DATA_ATTR_NAME).<String>get(ADDRESS_INVOICE_ATTR_NAME)), getLikeWildCard(searchDTO.getAddressInvoice())));
        }
        if(!StringUtils.isEmpty(searchDTO.getZipCodeInvoiceCode())){
            predicatesList.add(cb.like(cb.upper(from.<String>get(BASIC_DATA_ATTR_NAME).get(ZIP_CODE_INVOICE_ATTR_NAME).<String>get(ZipCode.ZIP_CODE_ATTR_NAME)), getLikeWildCard(searchDTO.getZipCodeInvoiceCode())));
        }
        if(!StringUtils.isEmpty(searchDTO.getZipCodeInvoiceCity())){
            predicatesList.add(cb.like(cb.upper(from.<String>get(BASIC_DATA_ATTR_NAME).get(ZIP_CODE_INVOICE_ATTR_NAME).<String>get(ZipCode.CITY_NAME_ATTR_NAME)), getLikeWildCard(searchDTO.getZipCodeInvoiceCity())));
        }

        if(searchDTO.getApplyDateFrom() != null){
            addDateFrom(cb, predicatesList, from.<Date>get(APPLY_DATE_ATTR_NAME), searchDTO.getApplyDateFrom());
        }
        if(searchDTO.getApplyDateTo() != null){
            addDateTo(cb, predicatesList, from.<Date>get(APPLY_DATE_ATTR_NAME), searchDTO.getApplyDateTo());
        }
        if(searchDTO.getConsiderationDateFrom() != null){
            addDateFrom(cb, predicatesList, from.<Date>get(CONSIDERATION_DATE_ATTR_NAME), searchDTO.getConsiderationDateFrom());
        }
        if(searchDTO.getConsiderationDateTo() != null){
            addDateTo(cb, predicatesList, from.<Date>get(CONSIDERATION_DATE_ATTR_NAME), searchDTO.getConsiderationDateTo());
        }
        Predicate[] predicatesTab = predicatesList.toArray(new Predicate[predicatesList.size()]);

        //SELECT
        cq.select(from).where(cb.and(predicatesTab));

        //SORT
        sort(searchDTO, cb, cq, from);

        //QUERY
        TypedQuery<GrantApplication> query = entityManager.createQuery(cq);

        //LIMIT
        limit(searchDTO, query);

        return query.getResultList();
    }
}
