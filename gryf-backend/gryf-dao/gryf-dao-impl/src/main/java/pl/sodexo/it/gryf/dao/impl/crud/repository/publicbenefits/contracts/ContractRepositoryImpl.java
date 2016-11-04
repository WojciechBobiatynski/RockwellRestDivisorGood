package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.contracts;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.common.dto.publicbenefits.contracts.searchform.ContractSearchQueryDTO;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.contracts.ContractRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.ContractType;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantOwner;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgram;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Isolution on 2016-10-27.
 */
@Repository
public class ContractRepositoryImpl extends GenericRepositoryImpl<Contract, Long> implements ContractRepository {

    @Override
    public List<Contract> findContracts(ContractSearchQueryDTO dto) {

        //FROM
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Contract> cq = cb.createQuery(entityClass);
        Root<Contract> from = cq.from(entityClass);

        //PREDICATE
        List<Predicate> predicatesList = new ArrayList<>();
        if(dto.getContractId() != null){
            predicatesList.add(cb.equal(from.get(Contract.ID_ATTR_NAME), dto.getContractId()));
        }
        if(!GryfStringUtils.isEmpty(dto.getContractTypeDescription())){
            predicatesList.add(cb.like(cb.upper(from.get(Contract.CONTRACT_TYPE_ATTR_NAME).<String>get(ContractType.NAME_ATTR_NAME)), getLikeWildCard(dto.getContractTypeDescription())));
        }
        if(!GryfStringUtils.isEmpty(dto.getPesel())){
            predicatesList.add(cb.like(cb.upper(from.get(Contract.INDIVIDUAL_ATTR_NAME).<String>get(Individual.PESEL_ATTR_NAME)), getLikeWildCard(dto.getPesel())));
        }
        if(!GryfStringUtils.isEmpty(dto.getVatRegNum())){
            predicatesList.add(cb.like(cb.upper(from.get(Contract.ENTERPRISE_ATTR_NAME).<String>get(Enterprise.VAT_REG_NUM_ATTR_NAME)), getLikeWildCard(dto.getVatRegNum())));
        }
        if(!GryfStringUtils.isEmpty(dto.getGrantProgramName())){
            predicatesList.add(cb.like(cb.upper(from.get(Contract.GRANT_PROGRAM_ATTR_NAME).<String>get(GrantProgram.PROGRAM_NAME_ATTR_NAME)), getLikeWildCard(dto.getGrantProgramName())));
        }
        if(!GryfStringUtils.isEmpty(dto.getGrantProgramOwnerName())){
            predicatesList.add(cb.like(cb.upper(from.get(Contract.GRANT_PROGRAM_ATTR_NAME).get(GrantProgram.PROGRAM_OWNER_ATTR_NAME).<String>get(GrantOwner.NAME_ATTR_NAME)), getLikeWildCard(dto.getGrantProgramOwnerName())));
        }
        if(dto.getSignDate() != null){
            addDateEqual(cb, predicatesList, from.<Date>get(Contract.SIGN_DATE_ATTR_NAME), dto.getSignDate());
        }
        if(dto.getExpiryDate() != null){
            addDateEqual(cb, predicatesList, from.<Date>get(Contract.EXPIRY_DATE_ATTR_NAME), dto.getExpiryDate());
        }

        Predicate[] predicatesTab = predicatesList.toArray(new Predicate[predicatesList.size()]);

        //SELECT
        cq.select(from).where(cb.and(predicatesTab));

        //SORT
        sort(dto, cb, cq, from);

        //QUERY
        TypedQuery<Contract> query = entityManager.createQuery(cq);

        //LIMIT
        limit(dto, query);

        return query.getResultList();
    }
}

