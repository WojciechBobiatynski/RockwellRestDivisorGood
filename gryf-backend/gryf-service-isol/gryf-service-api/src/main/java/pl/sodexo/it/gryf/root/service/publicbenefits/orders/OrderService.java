package pl.sodexo.it.gryf.root.service.publicbenefits.orders;

import pl.sodexo.it.gryf.common.dto.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.action.IncomingOrderElementDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.searchform.OrderSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.searchform.OrderSearchResultDTO;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface OrderService {

    /**
     * Metoda zwraca zserializowany obiekt OrderDTO, na podstawie którego generowany jest widok szczegółów zamówienia,
     * w którym mamy możliwosć podglądu.
     * @param id od zamówienia
     * @return serializowany objekt OrderDTO
     */
    String getOrderDataToModify(Long id);

    /**
     * Metoda zwraca zserializowany obiekt OrderDTO, na podstawie którego generowany jest widok szczegółów zamówienia,
     * w którym NIE MA możliwosći podglądu.
     * @param id od zamówienia
     * @return serializowany objekt OrderDTO
     */
    String getOrderDataToPreview(Long id);

    /**
     * Metoda zwraca listę zamówień na podstawie kryteriów wyszukiwania.
     * @param searchDTO obiekt wyszukujący
     * @return lista zamówień
     */
    List<OrderSearchResultDTO> findOrders(OrderSearchQueryDTO searchDTO);

    /**
     * Metoda tworzy i zapisuje do bazy obiekt zamowienia na podstawie wniosku.
     * @param grantApplication wniosek
     * @return zamowienie
     */
    Order createOrder(GrantApplication grantApplication);

    /**
     * Wykonanie akcji.
     * @param id id zamowienia
     * @param actionId id kacji
     * @param incomingOrderElements elementy z gui
     * @param files pliki które maja zasotać dodane
     * @param acceptedViolations błędy zaakceptowane przez użytkonika
     */
    void executeAction(Long id, Long actionId, Integer version, List<IncomingOrderElementDTO> incomingOrderElements, List<FileDTO> files, List<String> acceptedViolations);

    /**
     * Metoda zwraca objekt FIleDTO na podstawie zapisanego elementu. Zakłądamy że element do którego odnosi sie identyfikator
     * jest typu Attachement. W objekt FileDTO wypełniamy tylko pole name oraz inputStream poniewąz tylko tyle jest potrzebne do
     * wysłania pliku.
     * @param elementId identyfikator elementu zamówienia
     * @return objekt reprezentujacy załacznik
     */
    FileDTO getOrderAttachmentFile(Long elementId);

    /**
     * Metoda wywoływana gdy poleci błąd z OptimisticLockException. Pobiera aktualne dane i generuje wyjątek.
     * @param id identyfikator zamowienia
     */
    void manageLocking(Long id);

    /**
     * Dodaje konkretny element składowy zamówienia typu Varchar.
     * @param order zamówienie
     * @param elementId identyfikator elementu
     * @param valueVarchar vartość elementu typu varchar
     */
    void addElementVarcharValue(Order order, String elementId, String valueVarchar);

    /**
    * Dodaje konkretny element składowy zamówienia typu Number.
    * @param order zamówienie
    * @param elementId identyfikator elementu
    * @param valueNumber vartość elementu typu number
    */
    void addElementNumberValue(Order order, String elementId, BigDecimal valueNumber);

    /**
     * Dodaje konkretny element składowy zamówienia wybranego typu.
     * @param order zamówienie
     * @param elementId identyfikator elementu
     * @param valueNumber vartość elementu typu number
     * @param valueVarchar  wartość elementu typu string
     * @param valueDate wartość elementu typu date
     */
    void addElementWithValue(Order order, String elementId, BigDecimal valueNumber, String valueVarchar, Date valueDate);

    /**
     * Metoda zwraca kwotę do wpłaty przez przedsiębiorstwo (MŚP) za przyznane bony szkoniowe
     * @param order zamówienie, dla którego kwota ma zostać wyznaczona
     * @return
     */
    BigDecimal getPaymentAmount(Order order);
}
