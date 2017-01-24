package pl.sodexo.it.gryf.dao.api.search.dao;

import pl.sodexo.it.gryf.common.criteria.electronicreimbursements.ElctRmbsCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.*;

import java.util.List;

/**
 * Dao dla operacji na erozliczeniach
 *
 * Created by akmiecinski on 14.11.2016.
 */
public interface ElectronicReimbursementsDao {

    /**
     * Metoda zwracająca listę rozliczeń na podstawie kryteriów wyszkuwiania
     * @param criteria - kryteria wyszkuiwania
     * @return - lista rozliczeń
     */
    List<ElctRmbsDto> findEcltRmbsListByCriteria(ElctRmbsCriteria criteria);

    /**
     * Metoda zwracająca listę statusów rozliczeń
     * @return - lista statusów
     */
    List<SimpleDictionaryDto> findElctRmbsStatuses();

    /**
     * Metoda zwracająca listę typów rozliczeń
     * @return - lista typów
     */
    List<SimpleDictionaryDto> findElctRmbsTypes();

    /**
     * Metoda znajdująca parametry do obliczenia składek na rolizczeniu dla instancji szkolenia
     *
     * @param trainingInstanceId - id Usługodawcy
     * @return
     */
    CalculationChargesParamsDto findCalculationChargesParamsForTrInstId(Long trainingInstanceId);

    /**
     * Znajduje szczegóły rozliczenia dla bonów elektronicznych na podstawie Id
     * @param ermbsId - id rozliczenia
     * @return Dto rozliczenia
     */
    ElctRmbsHeadDto findEcltRmbsById(Long ermbsId);

    /**
     * Znajduje szczegóły rozliczenia podstawie Id instancji szkolenia
     * @param trainingInstanceId - id rozliczenia
     * @return Dto rozliczenia
     */
    ElctRmbsHeadDto findEcltRmbsByTrainingInstanceId(Long trainingInstanceId);

    /**
     * Znajduje parametry potrzbene do wypełnienia maila z rozliczeń
     * @param ermbsId - id rozliczenia
     * @return parametry pootrzebne do wypełnienia maila
     */
    ErmbsMailParamsDto findMailParams(Long ermbsId);

    /**
     * Znajduje parametry potrzbene do wypełnienia maila z rozliczeń
     * @param ermbsId - id rozliczenia
     * @return parametry pootrzebne do wypełnienia maila
     */
    ErmbsMailParamsDto findMailParamsForUnreservedPool(Long ermbsId);

    /**
     * Pobiera raporty wygenerowane dla rozliczenia jako dto plików
     * @param ermbsId - id rozliczenia
     * @return lista plików raportów
     */
    List<ErmbsMailAttachmentDto> findReportsByErmbsId(Long ermbsId);

    /**
     * Pobiera maile wysłane dla rozliczenia jako dto plików
     * @param ermbsId - id rozliczenia
     * @return lista plików raportów
     */
    List<ErmbsMailDto> findMailsByErmbsId(Long ermbsId);

    /**
     * Pobiera rozliczenie dla niewykorzystanej puli bonów
     * @param ermbsId - id rozliczenia
     * @return dto rozliczenia
     */
    UnrsvPoolRmbsDto findUnrsvPoolRmbsById(Long ermbsId);

    /**
     * Sprawdza czy podane rozliczenie jest dla MSP
     * @param ermbsId - id rozliczenia
     * @return true/false
     */
    Boolean isErmbsForEnterprise(Long ermbsId);

    /**
     * Sprawdza czy powinniśmy tworzyć notę uznanionową dla danego rozliczenia
     * @param ermbsId - id rozliczenia
     * @return true/false
     */
    boolean shouldBeCreditNoteCreated(Long ermbsId);

    /**
     * Sprawdza czy tworzone szkolenie jest tworzone po terminie
     * @param trainingInstanceId - id instancji szkolenia
     * @return true/false
     */
    boolean checkIBeingCreatedErmbsIsTerminatedByTrainingInstanceId(Long trainingInstanceId);

}
