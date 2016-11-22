package pl.sodexo.it.gryf.service.api.publicbenefits.orders;

import pl.sodexo.it.gryf.common.dto.other.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.CreateOrderDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.searchform.OrderSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.searchform.OrderSearchResultDTO;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface OrderService {

    /**
     * tworzy obiekt potrzebny do stworzenai zamowienia
     * @param contractId identyfikator umowy
     * @return obiekt dto
     */
    CreateOrderDTO createCreateOrderDTO(Long contractId);

    /**
     * Metoda tworzy i zapisuje obiekt zamowienia na podstawie dto.
     * @param dto parametr dto
     * @return identyfikator zamowienia
     */
    Long createOrder(CreateOrderDTO dto);

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
     * Zwraca listę ze statusami dla danego grant programu
     * @param grantProgramId
     * @return
     */
    List<DictionaryDTO> getOrderFlowStatusesByGrantProgram(Long grantProgramId);
}
