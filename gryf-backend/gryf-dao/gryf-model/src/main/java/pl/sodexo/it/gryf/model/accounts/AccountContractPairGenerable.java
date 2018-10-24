package pl.sodexo.it.gryf.model.accounts;

/**
 * Interfejs dla encji, które mają możliwość generowania numeru subkonta na podstawie kodu
 *
 * Created by akmiecinski on 01.12.2016.
 */
public interface AccountContractPairGenerable<ID> {

    ID getId();

    void setId(ID Id);

    String getCode();

    void setCode(String code);

    String getAccountPayment();

    void setAccountPayment(String accountPayment);

}
