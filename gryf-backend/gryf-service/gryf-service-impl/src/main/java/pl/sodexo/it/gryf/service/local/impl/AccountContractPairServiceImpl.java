package pl.sodexo.it.gryf.service.local.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.exception.EntityValidationException;
import pl.sodexo.it.gryf.common.other.GenerableCodeParams;
import pl.sodexo.it.gryf.dao.api.crud.repository.accounts.AccountContractPairRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.other.GryfPLSQLRepository;
import pl.sodexo.it.gryf.model.accounts.AccountContractPair;
import pl.sodexo.it.gryf.model.accounts.AccountContractPairGenerable;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
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

    @Autowired
    private ApplicationParameters applicationParameters;

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
    public AccountContractPair getValidAccountContractPairForUsedByContractId(String contractId) {
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
        }
        return account;
    }

    @Override
    public AccountContractPair use(AccountContractPair accountContractPair) {
        accountContractPair.setUsed(true);
        return accountContractPairRepository.update(accountContractPair, accountContractPair.getId());
    }

    @Override
    public String getCodeFromAccount(AccountContractPair pair) {
        return pair.getAccountPayment().substring(18);
    }

    @Override
    public AccountContractPairGenerable setIdAndAccountPayment(AccountContractPairGenerable entity) {
        String account = Strings.isNullOrEmpty(entity.getAccountPayment()) ? generateAccount(entity.getCode()) : entity.getAccountPayment();
        AccountContractPair accountContractPair = getValidAccountContractPairForUsed(account);
        use(accountContractPair);

        entity.setId(getIdFromGeneratedCode(entity));
        entity.setAccountPayment(account);
        return entity;
    }

    @Override
    public String generateCode(AccountContractPairGenerable entity) {
        GenerableCodeParams params = getParamsByType(entity);
        return String.format("%s%0" + params.getZeroCount() + "d", params.getPrefix(), entity.getId());
    }

    @Override
    public AccountContractPair findByContractId(String contractId) {
        return accountContractPairRepository.findByContractId(contractId);
    }

    private GenerableCodeParams getParamsByType(AccountContractPairGenerable entity){
        GenerableCodeParams params = new GenerableCodeParams();
        if(entity instanceof Enterprise){
            params.setPrefix(applicationParameters.getGryfEnterpriseCodePrefix());
            params.setZeroCount(applicationParameters.getGryfEnterpriseCodeZeroCount());
        } else {
            params.setPrefix(applicationParameters.getGryfIndividualCodePrefix());
            params.setZeroCount(applicationParameters.getGryfIndividualCodeZeroCount());
        }
        return params;
    }


    private Long getIdFromGeneratedCode(AccountContractPairGenerable entity) {
        GenerableCodeParams params = getParamsByType(entity);
        return Long.parseLong(entity.getCode().substring(params.getPrefix().length()));
    }
}
