package pl.sodexo.it.gryf.dao.api.search.mapper;

import org.apache.ibatis.annotations.Param;
import pl.sodexo.it.gryf.common.criteria.UserCriteria;
import pl.sodexo.it.gryf.common.criteria.electronicreimbursements.ElctRmbsCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.*;

import java.util.List;

/**
 * Mapper do operacji na e-rozliczeniach
 *
 * Created by akmiecinski on 14.11.2016.
 */
public interface ElectronicReimbursementsSearchMapper {

    /**
     * Metoda zwracająca listę rozliczeń na podstawie kryteriów wyszkuwiania
     *
     * @param criteria - kryteria wyszkuiwania
     * @return - lista rozliczeń
     */
    List<ElctRmbsDto> findEcltRmbsListByCriteria(@Param("criteria") ElctRmbsCriteria criteria);

    /**
     * Metoda zwracająca listę statusów rozliczeń
     *
     * @param criteria kryteria użytkownika
     * @return - lista statusów
     */
    List<SimpleDictionaryDto> findElctRmbsStatuses(@Param("criteria") UserCriteria criteria);

    /**
     * Metoda zwracająca listę typów rozliczeń
     *
     * @param criteria kryteria użytkownika
     * @return - lista typów
     */
    List<SimpleDictionaryDto> findElctRmbsTypes(@Param("criteria") UserCriteria criteria);

    /**
     * Metoda znajdująca parametry do obliczenia składek na rolizczeniu dla instancji usługi
     *
     * @param criteria - kryteria użytkownika
     * @param trainingInstanceId - id Usługodawcy
     * @return
     */
    CalculationChargesParamsDto findCalculationChargesParamsForTrInstId(@Param("criteria") UserCriteria criteria, @Param("trainingInstanceId") Long trainingInstanceId);

    /**
     * Znajduje szczegóły rozliczenia dla bonów elektronicznych na podstawie Id
     * @param criteria - krytertia użytkownika
     * @param ermbsId - id rozliczenia
     * @return Dto rozliczenia
     */
    ElctRmbsHeadDto findEcltRmbsById(@Param("criteria") UserCriteria criteria, @Param("ermbsId") Long ermbsId);

    /**
     * Znajduje szczegóły rozliczenia podstawie Id instancji usługi
     * @param criteria - krytertia użytkownika
     * @param trainingInstanceId - id rozliczenia
     * @return Dto rozliczenia
     */
    ElctRmbsHeadDto findEcltRmbsByTrainingInstanceId(@Param("criteria") UserCriteria criteria, @Param("trainingInstanceId") Long trainingInstanceId);

    /**
     * Znajduje parametry potrzbene do wypełnienia maila z rozliczeń
     * @param criteria - krytertia użytkownika
     * @param ermbsId - id rozliczenia
     * @return parametry pootrzebne do wypełnienia maila
     */
    ErmbsMailParamsDto findMailParams(@Param("criteria") UserCriteria criteria, @Param("ermbsId") Long ermbsId);

    /**
     * Znajduje parametry programu dofinansowania na podstawie id rozliczenia
     * @param criteria - krytertia użytkownika
     * @param ermbsId - id rozliczenia
     * @return
     */
    ErmbsGrantProgramParamsDto findGrantProgramParams(@Param("criteria") UserCriteria criteria, @Param("ermbsId") Long ermbsId);

    /**
     * Znajduje parametry potrzbene do wypełnienia maila z rozliczeń dla niewykorzystanej puli bonów
     * @param criteria - krytertia użytkownika
     * @param ermbsId - id rozliczenia
     * @return parametry pootrzebne do wypełnienia maila
     */
    ErmbsMailParamsDto findMailParamsForUnreservedPool(@Param("criteria") UserCriteria criteria, @Param("ermbsId") Long ermbsId);

    /**
     * Pobiera raporty wygenerowane dla rozliczenia jako dto plików
     * @param criteria - krytertia użytkownika
     * @param ermbsId - id rozliczenia
     * @return lista plików raportów
     */
    List<ErmbsMailAttachmentDto> findReportsByErmbsId(@Param("criteria") UserCriteria criteria, @Param("ermbsId") Long ermbsId);

    /**
     * Pobiera maile wysłane dla rozliczenia jako dto plików
     * @param criteria - krytertia użytkownika
     * @param ermbsId - id rozliczenia
     * @return lista plików raportów
     */
    List<ErmbsMailDto> findMailsByErmbsId(@Param("criteria") UserCriteria criteria, @Param("ermbsId") Long ermbsId);

    /**
     * Pobiera rozliczenie dla niewykorzystanej puli bonów
     * @param criteria - kryteria wyszukiwania
     * @param ermbsId - id rozliczenia
     * @return dto rozliczenia
     */
    UnrsvPoolRmbsDto findUnrsvPoolRmbsById(@Param("criteria") UserCriteria criteria, @Param("ermbsId") Long ermbsId);

    /**
     * Sprawdza czy podane rozliczenie jest dla MSP
     * @param criteria - kryteria użytkownika
     * @param ermbsId - id rozliczenia
     * @return true/false
     */
    Boolean isErmbsForEnterprise(@Param("criteria") UserCriteria criteria, @Param("ermbsId") Long ermbsId);

    /**
     * Sprawdza czy powinniśmy tworzyć notę uznanionową dla danego rozliczenia
     * @param ermbsId - id rozliczenia
     * @return true/false
     */
    boolean shouldBeCreditNoteCreated(@Param("criteria") UserCriteria criteria, @Param("ermbsId")Long ermbsId);

    /**
     * Sprawdza czy tworzone usługa jest tworzone po terminie
     * @param criteria - kryteria użytkownika
     * @param trainingInstanceId - id instancji usługi
     * @return true/false
     */
    boolean checkIBeingCreatedErmbsIsTerminatedByTrainingInstanceId(@Param("criteria") UserCriteria criteria, @Param("trainingInstanceId") Long trainingInstanceId);

    /**
     * Sprawdczy czy rozliczenie powinno być wykonane automatycznie
     * @param criteria - kryteria użytkownika
     * @param ermbsId - id rozliczenia
     * @return true/false
     */
    boolean isAutomaticErmbs(@Param("criteria") UserCriteria criteria, @Param("ermbsId")Long ermbsId);

    /**
     * Pobiera nazwe statusu danego rozlicznenia
     * @param criteria - kryteria użytkownika
     * @param ermbsId identyfikator rozliczneia
     * @return nazwa statusu
     */
    String getReimbursmentStatusName(@Param("criteria") UserCriteria criteria, @Param("ermbsId")Long ermbsId);

}
