package pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements;

import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.UnrsvPoolRmbsDto;

/**
 * Serwis do operacji na rozliczeniach niewykorzystanej puli bonów
 *
 * Created by akmiecinski on 14.11.2016.
 */
public interface UnreservedPoolReimbursementService {

    /**
     * Job, który sprawdza, czy jest jakaś przeterminowana pula bonów i jeśli tak to tworzy dla niej rozliczenie,
     * tworzy dokumenty, rozlicza pulę i drukuje raporty. Działa nietransakcyjnie.
     */
    void createReimbursementForExpiredInstancesPool();

    /**
     * Pobiera rozliczenie dla niewykorzystanej puli bonów
     * @param ermbsId - id rozliczenia
     * @return dto rozliczenia
     */
    UnrsvPoolRmbsDto findUnrsvPoolRmbsById(Long ermbsId);

}
