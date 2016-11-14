package pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements;

import pl.sodexo.it.gryf.common.criteria.electronicreimbursements.ElctRmbsCriteria;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsDto;

import java.util.List;

/**
 * Serwis do operacji na e-rozliczeniach
 *
 * Created by akmiecinski on 14.11.2016.
 */
public interface ElectronicReimbursementsService {

    /**
     * Metoda zwracająca listę rozliczeń na podstawie kryteriów wyszkuwiania
     * @param criteria - kryteria wyszkuiwania
     * @return - lista rozliczeń
     */
    List<ElctRmbsDto> findEcltRmbsListByCriteria(ElctRmbsCriteria criteria);

}
