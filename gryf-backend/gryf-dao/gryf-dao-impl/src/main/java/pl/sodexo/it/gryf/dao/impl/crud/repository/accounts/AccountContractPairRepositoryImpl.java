package pl.sodexo.it.gryf.dao.impl.crud.repository.accounts;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.dao.api.crud.repository.accounts.AccountContractPairRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.accounts.AccountContractPair;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 * Implementacja repozytoyium dla encji pary subkonto-umowa
 *
 * Created by akmiecinski on 02.11.2016.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class AccountContractPairRepositoryImpl extends GenericRepositoryImpl<AccountContractPair, Long> implements AccountContractPairRepository {

    @Override
    public AccountContractPair findByAccountPayment(String accountPayment) {
        try {
            TypedQuery<AccountContractPair> query = entityManager.createNamedQuery(AccountContractPair.FIND_BY_ACCOUNT_PAYMENT, AccountContractPair.class);
            query.setParameter("accountPayment", accountPayment);
            return query.getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }

    @Override
    public String findAccountByCode(String code) {
        Query query = entityManager.createNativeQuery("SELECT eagle.t$bank_account.GET_COR_SPP(?1) FROM dual");
        query.setParameter(1, code);
        return (String) query.getSingleResult();
    }

    @Override
    public AccountContractPair findByContractId(Long contractId) {
        try {
            TypedQuery<AccountContractPair> query = entityManager.createNamedQuery(AccountContractPair.FIND_BY_CONTRACT_ID, AccountContractPair.class);
            query.setParameter("contractId", contractId);
            return query.getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }

}
