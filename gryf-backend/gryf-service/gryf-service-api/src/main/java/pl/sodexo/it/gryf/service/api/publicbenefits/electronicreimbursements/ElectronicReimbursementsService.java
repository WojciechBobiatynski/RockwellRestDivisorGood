package pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements;

import pl.sodexo.it.gryf.common.criteria.electronicreimbursements.ElctRmbsCriteria;
import pl.sodexo.it.gryf.common.dto.api.SimpleDictionaryDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.CorrectionDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.electronicreimbursements.ElctRmbsHeadDto;

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
     * Tworzy nowe rozliczenie na podstawie Id instancji szkolenia
     * @param trainingInstanceId - id instancji szkolenia
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
     * @param rmbsId
     */
    void confirm(Long rmbsId);

    /**
     * Rozlicza niewykorzystaną pule bonów
     * @param rmbsId
     */
    void expire(Long rmbsId);
}
