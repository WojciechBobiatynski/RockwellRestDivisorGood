package pl.sodexo.it.gryf.service.local.api;

import pl.sodexo.it.gryf.model.accounts.AccountContractPair;

import java.util.Arrays;

/**
 * Created by Isolution on 2016-12-01.
 */
public interface AccountContractPairService {

    AccountContractPair getValidAccountContractPairForUsed(String account);

    AccountContractPair getValidAccountContractPairForUsed(Long contractId);

    String generateAccount(String code);

    AccountContractPair use(AccountContractPair accountContractPair);

    String getCodeFromAccount(AccountContractPair pair);
}
