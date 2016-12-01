package pl.sodexo.it.gryf.service.local.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.exception.EntityValidationException;
import pl.sodexo.it.gryf.dao.api.crud.repository.accounts.AccountContractPairRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.other.GryfPLSQLRepository;
import pl.sodexo.it.gryf.model.accounts.AccountContractPair;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.service.local.api.AccountContractPairService;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;

import javax.persistence.PersistenceException;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Created by Isolution on 2016-12-01.
 */
@Service
public class AccountContractPairServiceImpl implements AccountContractPairService {

    //STATIC FIELDS

    private static final int INVALID_ACCOUNT_FORMAT_ERROR_CODE = 20002;

    //PRIVATE FIELDS

    @Autowired
    private GryfValidator gryfValidator;

    @Autowired
    private AccountContractPairRepository accountContractPairRepository;

    @Autowired
    private GryfPLSQLRepository gryfPLSQLRepository;

    //PUBLIC METHODS

    @Override
    public AccountContractPair getValidAccountContractPairForUsed(String account) {
        AccountContractPair accountContractPair = accountContractPairRepository.findByAccountPayment(account);
        if (accountContractPair == null) {
            gryfValidator.validate(Individual.CODE_ATTR_NAME, "Niepoprawny kod osoby fizycznej");
        }
        if (accountContractPair.isUsed()) {
            gryfValidator.validate(Individual.CODE_ATTR_NAME, "Wpisany kod jest już zarezerwowany");
        }
        return accountContractPair;
    }

    @Override
    public AccountContractPair getValidAccountContractPairForUsed(Long contractId) {
        AccountContractPair accountContractPair = accountContractPairRepository.findByContractId(contractId);
        if (accountContractPair == null) {
            gryfValidator.validate(Individual.CODE_ATTR_NAME, "Niepoprawne znalezione powiązanie idnetyfikatora umowy oraz konta");
        }
        if (accountContractPair.isUsed()) {
            gryfValidator.validate(Individual.CODE_ATTR_NAME, "Wpisana para identyfiktor umowy - konto jest już zarezerwowana");
        }
        return accountContractPair;
    }

    //TODO:przepisać na mybatisa, wtedy bez tego bardzo brzydkiego catcha
    @Override
    public String generateAccount(String code) {
        String account = null;
        try {
            account = gryfPLSQLRepository.generateAccountByCode(code);
        } catch (PersistenceException e) {
            Throwable exc = e.getCause().getCause();
            if (exc instanceof SQLException) {
                if (((SQLException) exc).getErrorCode() == INVALID_ACCOUNT_FORMAT_ERROR_CODE) {
                    EntityConstraintViolation entityConstraintViolation = new EntityConstraintViolation(Individual.CODE_ATTR_NAME, "Niepoprawny format kodu osoby fizycznej");
                    throw new EntityValidationException(Arrays.asList(entityConstraintViolation));
                }
            }
            ;
        }
        return account;
    }

    @Override
    public AccountContractPair use(AccountContractPair accountContractPair){
        accountContractPair.setUsed(true);
        return accountContractPairRepository.update(accountContractPair, accountContractPair.getId());
    }

    @Override
    public String getCodeFromAccount(AccountContractPair pair){
        return pair.getAccountPayment().substring(18);
    }
}
