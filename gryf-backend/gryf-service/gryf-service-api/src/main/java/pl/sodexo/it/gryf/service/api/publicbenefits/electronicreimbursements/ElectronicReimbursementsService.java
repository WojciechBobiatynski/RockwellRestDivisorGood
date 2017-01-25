package pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements;

import pl.sodexo.it.gryf.common.criteria.electronicreimbursements.ElctRmbsCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CorrectionDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.RejectionDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.pbeproductinstancepool.PbeProductInstancePoolDto;

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
     * Tworzy nowe rozliczenie na podstawie Id instancji usługi
     * @param trainingInstanceId - id instancji usługi
     * @return id rozliczenia
     */
    ElctRmbsHeadDto createRmbsDtoByTrainingInstanceId(Long trainingInstanceId);

    /**
     * Znajduje szczegóły rozliczenia dla bonów elektronicznych na podstawie Id
     * @param ermbsId - id rozliczenia
     * @return Dto rozliczenia
     */
    ElctRmbsHeadDto findEcltRmbsById(Long ermbsId);

    /**
     * Metoda zapisująca rozliczenia dla bonów
     * @param elctRmbsHeadDto - dto rozliczenia
     * @return id zapisanego obiektu
     */
    Long saveErmbs(ElctRmbsHeadDto elctRmbsHeadDto);

    /**
     * Metoda wysyłająca rozliczenie do Operatora Finansowego
     * @param elctRmbsHeadDto - dto rozliczenia
     * @return id zapisanego obiektu
     */
    Long sendToReimburse(ElctRmbsHeadDto elctRmbsHeadDto);

    /**
     * Metoda zapisująca rozliczenia dla bonów z korektą
     * @param elctRmbsHeadDto - dto rozliczenia
     * @return id zapisanego obiektu
     */
    Long saveErmbsWithCorr(ElctRmbsHeadDto elctRmbsHeadDto);

    /**
     * Metoda wysyłająca rozliczenie po korekcie do Operatora Finansowego
     * @param elctRmbsHeadDto - dto rozliczenia
     * @return id zapisanego obiektu
     */
    Long sendToReimburseWithCorr(ElctRmbsHeadDto elctRmbsHeadDto);

    /**
     * Metoda ustawia status rozliczenia na "do korekty".
     * @param correctionDto - dto zawierające informacje o powodzie korekcie
     * @return wersja rozliczenia
     */
    Long sendToCorrect(CorrectionDto correctionDto);

    /**
     * Generuje odpowiednie dokumenty dla rozliczenia bonów elektronicznych
     * @param rmbsId
     * @return id zapisanego obiektu
     */
    Long createDocuments(Long rmbsId);

    /**
     * Drukuje raporty jasperowe dla rozliczenia bonów elektronicznych
     * @param rmbsId
     * @return id zapisanego obiektu
     */
    Long printReports(Long rmbsId);

    /**
     * Anuluje rozliczenie
     * @param rmbsId
     * @return id zapisanego obiektu
     */
    Long cancel(Long rmbsId);

    /**
     * Potwierdza rozlicznie
     * @param rmbsId - id rozliczenia
     * @return id rozliczenia zaktualizowanego
     */
    Long confirm(Long rmbsId);

    /**
     * Rozlicza niewykorzystaną pule bonów
     * @param rmbsId - id rozliczenia
     * @return id rozliczenia zaktualizowanego
     */
    Long expire(Long rmbsId);

    /**
     * Sprawdza czy rozlicznei jest w ramach Usługodawcy zalogowanego zalogowanego użytkownika
     * @param ereimbursementId
     * @return
     */
    boolean isEreimbursementInLoggedUserInstitution(Long ereimbursementId);

    /**
     * Tworzy rozliczenie dla niwykorzytsanej puli bonów
     * @param pbeProductInstancePool - dto instancji puli bonó
     * @return - id rozliczenia
     */
    Long createEreimbursementForUnrsvPool(PbeProductInstancePoolDto pbeProductInstancePool);

    /**
     * Odrzuca rozliczenie
     * @param rejectionDto - dto odrzucenia
     * @return id rozliczenia zaktualizowanego
     */
    Long reject(RejectionDto rejectionDto);

    /**
     * Sprawdza czy dla danego rozliczenia ma być automatyczny proces
     * @param ermbs
     * @return
     */
    boolean isAutomaticErmbs(Long ermbs);

}
