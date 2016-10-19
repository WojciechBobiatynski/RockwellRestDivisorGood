package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.enterprises;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.searchform.EnterpriseSearchQueryDTO;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.enterprises.EnterpriseRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-06-22.
 */
@Repository
public class EnterpriseRepositoryImpl extends GenericRepositoryImpl<Enterprise, Long> implements EnterpriseRepository {

    @Override
    public List<Enterprise> findByVatRegNum(String vatRegNum){
        TypedQuery<Enterprise> query = entityManager.createNamedQuery(Enterprise.FIND_BY_VAT_REG_NUM, Enterprise.class);
        query.setParameter("vatRegNum", vatRegNum);
        return query.getResultList();
    }

    @Override
    public Enterprise getForUpdate(Long id) {
        TypedQuery<Enterprise> query = entityManager.createNamedQuery(Enterprise.GET_FOR_UPDATE, Enterprise.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public List<Enterprise> findEnterprises(EnterpriseSearchQueryDTO dto) {

        //FROM
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Enterprise> cq = cb.createQuery(entityClass);
        Root<Enterprise> from = cq.from(entityClass);

        //PREDICATE
        List<Predicate> predicatesList = new ArrayList<>();
        if(dto.getId() != null){
            predicatesList.add(cb.equal(from.get(Enterprise.ID_ATTR_NAME), dto.getId()));
        }
        if(!GryfStringUtils.isEmpty(dto.getName())){
            predicatesList.add(cb.like(cb.upper(from.<String>get(Enterprise.NAME_ATTR_NAME)), getLikeWildCard(dto.getName())));
        }
        if(!GryfStringUtils.isEmpty(dto.getVatRegNum())){
            predicatesList.add(cb.like(cb.upper(from.<String>get(Enterprise.VAT_REG_NUM_ATTR_NAME)), getLikeWildCard(dto.getVatRegNum())));
        }
        if(!GryfStringUtils.isEmpty(dto.getAddressInvoice())){
            predicatesList.add(cb.like(cb.upper(from.<String>get(Enterprise.ADDRESS_INVOICE_ATTR_NAME)), getLikeWildCard(dto.getAddressInvoice())));
        }
        if(!GryfStringUtils.isEmpty(dto.getZipCodeInvoiceCode())){
            predicatesList.add(cb.like(cb.upper(from.get(Enterprise.ZIP_CODE_INVOICE_ATTR_NAME).<String>get(ZipCode.ZIP_CODE_ATTR_NAME)), getLikeWildCard(dto.getZipCodeInvoiceCode())));
        }
        if(!GryfStringUtils.isEmpty(dto.getZipCodeInvoiceCity())){
            predicatesList.add(cb.like(cb.upper(from.get(Enterprise.ZIP_CODE_INVOICE_ATTR_NAME).<String>get(ZipCode.CITY_NAME_ATTR_NAME)), getLikeWildCard(dto.getZipCodeInvoiceCity())));
        }
        if(!GryfStringUtils.isEmpty(dto.getAddressCorr())){
            predicatesList.add(cb.like(cb.upper(from.<String>get(Enterprise.ADDRESS_CORR_ATTR_NAME)), getLikeWildCard(dto.getAddressCorr())));
        }
        if(!GryfStringUtils.isEmpty(dto.getZipCodeCorrCode())){
            predicatesList.add(cb.like(cb.upper(from.get(Enterprise.ZIP_CODE_CORR_ATTR_NAME).<String>get(ZipCode.ZIP_CODE_ATTR_NAME)), getLikeWildCard(dto.getZipCodeCorrCode())));
        }
        if(!GryfStringUtils.isEmpty(dto.getZipCodeCorrCity())){
            predicatesList.add(cb.like(cb.upper(from.get(Enterprise.ZIP_CODE_CORR_ATTR_NAME).<String>get(ZipCode.CITY_NAME_ATTR_NAME)), getLikeWildCard(dto.getZipCodeCorrCity())));
        }
        Predicate[] predicatesTab = predicatesList.toArray(new Predicate[predicatesList.size()]);

        //SELECT
        cq.select(from).where(cb.and(predicatesTab));

        //SORT
        sort(dto, cb, cq, from);

        //QUERY
        TypedQuery<Enterprise> query = entityManager.createQuery(cq);

        //LIMIT
        limit(dto, query);

        return query.getResultList();
    }

}
