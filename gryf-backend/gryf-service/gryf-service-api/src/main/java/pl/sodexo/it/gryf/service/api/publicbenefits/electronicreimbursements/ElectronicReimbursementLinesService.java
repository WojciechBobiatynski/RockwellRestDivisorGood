package pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements;

import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CalculationChargesOrderParamsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsLineDto;

import java.util.List;

/**
 * Serwis do operacji na liniach częściowych e-rozliczeń
 * <p>
 * Created by dptaszynski on 14.07.2020.
 */
public interface ElectronicReimbursementLinesService {

    /**
     * Metoda zapisująca lini częściowych rozliczeń
     *
     * @param elctRmbsLineDtos - DTO do zapisania
     * @return - id
     */
    Long saveEreimbursementLine(ElctRmbsLineDto elctRmbsLineDtos);

    /**
     * Metoda zwracająca listę id lini rozliczeń
     *
     * @return - lista statusów
     */
    List<Long> getElctRmbsLineIds(ElctRmbsHeadDto elctRmbsHeadDto, List<CalculationChargesOrderParamsDto> orderParams);

}
