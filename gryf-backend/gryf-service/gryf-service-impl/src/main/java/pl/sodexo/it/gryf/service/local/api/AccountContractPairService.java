package pl.sodexo.it.gryf.service.local.api;

import pl.sodexo.it.gryf.model.accounts.AccountContractPair;
import pl.sodexo.it.gryf.model.accounts.AccountContractPairGenerable;

/**
 * Created by Isolution on 2016-12-01.
 */
public interface AccountContractPairService {

    AccountContractPair getValidAccountContractPairForUsed(String account);

    AccountContractPair getValidAccountContractPairForUsed(Long contractId);

    String generateAccount(String code);

    AccountContractPair use(AccountContractPair accountContractPair);

    String getCodeFromAccount(AccountContractPair pair);

    /**
     * Metoda, która ustawia id i numer konta dla danego typu podmiotu (OsFizs/MŚP) na podstawie wygenerowanej tabeli z parami numer konta - kod
     * @param entity - encja typu podmiotu (MŚP/OsFiz)
     * @return - zaktualizowana encja
     */
    AccountContractPairGenerable setIdAndAccountPayment(AccountContractPairGenerable entity);

    /**
     * Generuje kod typu podmiotu dla przekazanego typu (OsFiz/MŚP)
     * @param entity - encja typu podmiotu
     * @return wygenerowany kod
     */
    String generateCode(AccountContractPairGenerable entity);
}
