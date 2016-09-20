package pl.sodexo.it.gryf.root.service.publicbenefits.reimbursement;

import pl.sodexo.it.gryf.dto.publicbenefits.reimbursement.detailsform.ReimbursementDTO;

/**
 * Created by jbentyn on 2016-09-20.
 */
//TODO Zmiana nazwy na ReimbursementService
public interface ReimbursementV1Service {

    /**
     * Metoda wyszukuje objekt transferowy dla isteniejącego zamówienia.
     * @param id identyfikator zamowienia
     * @return obiekt transferowy zamówienia
     */
    ReimbursementDTO findReimbursement(Long id);

    ReimbursementDTO createInitialReimbursement(Long reimbursementDeliveryId);

    /**
     * Metoda która ustawia status 'Zaanonsowane'.
     * @param dto obiekt transferowy dla zamówienia
     * @return identyfikator zamowienia
     */
    Long saveReimbursement(ReimbursementDTO dto);

    /**
     * Metoda która ustawia status 'Korygowane'.
     * @param dto obiekt transferowy dla zamówienia
     * @return identyfikator zamowienia
     */
    Long correctReimbursement(ReimbursementDTO dto);

    /**
     * Metoda która ustawia status 'Do weryfikacji'.
     * @param dto obiekt transferowy dla zamówienia
     * @return identyfikator zamowienia
     */
    Long verifyReimbursement(ReimbursementDTO dto);

    /**
     * Metoda która ustawia status 'Do rozliczenia'.
     * @param dto obiekt transferowy dla zamówienia
     * @return identyfikator zamowienia
     */
    Long settleReimbursement(ReimbursementDTO dto);

    /**
     * Genruje potwierdzenie dofinansowania. Metoda zapisuje dane transferDate oraz remarks
     * następnie genruje raport i dołącza jako załacznik do rozliczenia
     * @param dto obiekt transferowy
     * @return identyfikator zamowienia
     */
    Long generateConfirmationReimbursement(ReimbursementDTO dto);

    /**
     * Metoda która ustawia status 'Zakończone'.
     * @param dto obiekt transferowy dla zamówienia
     * @return identyfikator zamowienia
     */
    Long completeReimbursement(ReimbursementDTO dto);

    /**
     * Metoda która ustawia status 'Anulowane'. Nie zapisuje zmian w bazie (oprócz statusu).
     * @param dto obiekt transferowy dla zamówienia
     * @return identyfikator zamowienia
     */
    Long cancelReimbursement(ReimbursementDTO dto);

    void manageLocking(Long id);
}
