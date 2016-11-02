package pl.sodexo.it.gryf.dao.api.crud.repository.accounts;

import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.accounts.AccountContractPair;

/**
 * Repozytorium dla encji pary subkonto-umowa
 *
 * Created by akmiecinski on 02.11.2016.
 */
public interface AccountContractPairRepository extends GenericRepository<AccountContractPair, Long> {

    /**
     * Znajduje parę umowa-konto na podstawie subkonta
     * @param accountPayment - numer subkonta
     * @return para umowa-konto
     */
    AccountContractPair findByAccountPayment(String accountPayment);

    /**
     * Generuje numer konta na podstawie kodu
     * @param code - kod
     * @return numer konta
     */
    String findAccountByCode(String code);

}