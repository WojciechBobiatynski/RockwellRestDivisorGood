package pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements;

/**
 * Serwis do automatycznych operacji na e-rozliczeniach
 *
 * Created by akmiecinski on 14.11.2016.
 */
public interface AutomaticElectronicReimbursementsService {

    /**
     * Metoda, która automatycznie rozlicza rozliczenie. Działa nietransakcyjnie
     * @param ermbsId - id rozliczenia
     * @return - id zaktualizowego rozliczenia
     */
    Long reimburse(Long ermbsId);
}
