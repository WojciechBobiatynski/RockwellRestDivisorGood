package pl.sodexo.it.gryf.root.service.publicbenefits.orders.elements.elementTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.elements.OrderElementComplexTypeGrantedVouchersInfoDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.elements.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationBasicData;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramLimit;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowElement;
import pl.sodexo.it.gryf.root.repository.publicbenefits.grantprograms.GrantProgramLimitRepository;
import pl.sodexo.it.gryf.root.repository.publicbenefits.orders.OrderElementRepository;
import pl.sodexo.it.gryf.root.repository.publicbenefits.orders.OrderFlowElementRepository;
import pl.sodexo.it.gryf.root.repository.publicbenefits.orders.OrderRepository;
import pl.sodexo.it.gryf.root.service.publicbenefits.orders.elements.OrderElementBaseService;
import pl.sodexo.it.gryf.utils.GryfUtils;

import java.math.BigDecimal;
import java.util.*;
import pl.sodexo.it.gryf.root.service.publicbenefits.orders.OrderService;

/**
 * Created by tomasz.bilski.ext on 2015-08-27.
 */
@Service
public class OrderElementComplexTypeGrantedVouchersInfoService extends OrderElementBaseService<OrderElementComplexTypeGrantedVouchersInfoDTO> {


    //STATIC FIELDS

    /**
     * Klucz dla pola: Przysługująca liczba bonów
     */
    public static final String ENTITLED_VOUCHERS_NUMBER_ELEM_ID = "VOU_NUM_E";

    /**
     * Klucz dla pola: Przysługująca wysokość pomocy w PLN
     */
    public static final String ENTITLED_PLN_AMOUNT_ELEM_ID = "GR_AM_ENTL";

    /**
     * Klucz dla pola: Limit pomocy de minimis w EUR
     */
    public static final String LIMIT_EUR_AMOUNT_ELEM_ID = "GR_LIM_EUR";

    /**
     * Klucz dla pola: Kurs EUR
     */
    public static final String EXCHANGE_ELEM_ID = "EUR_EXC_RT";

    public static final List<String> LIST_ELEM_ID  = new ArrayList<>();

    static{
        LIST_ELEM_ID.add(ENTITLED_VOUCHERS_NUMBER_ELEM_ID);
        LIST_ELEM_ID.add(ENTITLED_PLN_AMOUNT_ELEM_ID);
        LIST_ELEM_ID.add(LIMIT_EUR_AMOUNT_ELEM_ID);
        LIST_ELEM_ID.add(EXCHANGE_ELEM_ID);
    }

    //FIELDS

    @Autowired
    private OrderService orderService;
        
    @Autowired
    private OrderElementRepository orderElementRepository;

    @Autowired
    private GrantProgramLimitRepository grantProgramLimitRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    //PUBLIC METHODS

    @Override
    public OrderElementComplexTypeGrantedVouchersInfoDTO createElement(OrderElementDTOBuilder builder) {
        Order order = builder.getOrder();

        Map<String, OrderElement> elementMap = orderElementRepository.findByOrderAndElements(order.getId(), LIST_ELEM_ID);
        checkContainsMap(order, elementMap);

        Integer entitledVouchersNumber = (elementMap.get(ENTITLED_VOUCHERS_NUMBER_ELEM_ID).getValueNumber() != null) ?
                                                elementMap.get(ENTITLED_VOUCHERS_NUMBER_ELEM_ID).getValueNumber().intValue() : null;
        BigDecimal entitledPlnAmount = elementMap.get(ENTITLED_PLN_AMOUNT_ELEM_ID).getValueNumber();
        BigDecimal limitEurAmount = elementMap.get(LIMIT_EUR_AMOUNT_ELEM_ID).getValueNumber();
        BigDecimal exchange = elementMap.get(EXCHANGE_ELEM_ID).getValueNumber();

        return new OrderElementComplexTypeGrantedVouchersInfoDTO(builder, entitledVouchersNumber,
                                                        entitledPlnAmount, limitEurAmount, exchange);
    }

    @Override
    public void updateValue(OrderElement element, OrderElementComplexTypeGrantedVouchersInfoDTO dto) {

        Order order = element.getOrder();
        Map<String, OrderElement> elementMap = orderElementRepository.findByOrderAndElements(order.getId(), LIST_ELEM_ID);

        updateElement(order, elementMap, ENTITLED_VOUCHERS_NUMBER_ELEM_ID, (dto.getEntitledVouchersNumber() != null) ?
                                                                new BigDecimal(dto.getEntitledVouchersNumber()) : null);
        updateElement(order, elementMap, ENTITLED_PLN_AMOUNT_ELEM_ID, dto.getEntitledPlnAmount());
        updateElement(order, elementMap, LIMIT_EUR_AMOUNT_ELEM_ID, dto.getLimitEurAmount());
        updateElement(order, elementMap, EXCHANGE_ELEM_ID, dto.getExchange());
        order.setVouchersNumber(dto.getGivenVouchersNumber());

        element.setCompletedDate(new Date());
    }

    /**
     * Dodaje elementy do zamówienia które przetrzymuja informacje voucherach.
     * @param order zamówienie
     */
    public void addVouchersInfoElements(Order order){
        GrantApplication application = order.getApplication();

        /* wyznacz przysługującą ilość bonów dla przedsiębiorstwa */
        Long entitledVouchNumber = getEntitledVouchersNumber(order, application);
                
        orderService.addElementNumberValue(order, ENTITLED_VOUCHERS_NUMBER_ELEM_ID, new BigDecimal(entitledVouchNumber));
        orderService.addElementNumberValue(order, ENTITLED_PLN_AMOUNT_ELEM_ID,  order.getProduct().getPbeAidValue().multiply(new BigDecimal(entitledVouchNumber)));
        orderService.addElementNumberValue(order, LIMIT_EUR_AMOUNT_ELEM_ID, null);
        orderService.addElementNumberValue(order, EXCHANGE_ELEM_ID, null);
    }

    
    //PROTECTED METHODS - OVERRIDE VALIDATION

    @Override
    protected boolean isValueInserted(OrderElement orderElement, OrderElementComplexTypeGrantedVouchersInfoDTO dto){
        Integer valOld = orderElement.getOrder().getVouchersNumber();
        Integer valNew = dto.getGivenVouchersNumber();
        return valOld == null && valNew != null;
    }

    @Override
    protected boolean isValueUpdated(OrderElement orderElement, OrderElementComplexTypeGrantedVouchersInfoDTO dto){
        Integer valOld = orderElement.getOrder().getVouchersNumber();
        Integer valNew = dto.getGivenVouchersNumber();
        return !Objects.equals(valOld, valNew);
    }

    @Override
    protected boolean isValueFilled(OrderElement orderElement, OrderElementComplexTypeGrantedVouchersInfoDTO dto){
        Integer valNew = dto.getGivenVouchersNumber();
        return valNew != null;
    }

    @Override
    protected void addViolation(List<EntityConstraintViolation> violations, OrderElementComplexTypeGrantedVouchersInfoDTO dto, String message){
        violations.add(new EntityConstraintViolation(dto.getOrderFlowElementId(), message, dto.getGivenVouchersNumber()));
    }    
    
    //PRIVATE METHODS

    /**
     * Sprawdza czy mapa zawiera wszystkie potrzebne elementy
     * @param order zamówienie
     * @param elementMap mapa z elementami - klucz ELEMENT_D, obiekt - OrderElement
     */
    private void checkContainsMap(Order order, Map<String, OrderElement> elementMap){
        if (!elementMap.containsKey(ENTITLED_VOUCHERS_NUMBER_ELEM_ID)){
            throw new RuntimeException(String.format("Błąd konfiguracji - nie istnieje pole w '%s' dla zamówienia '%s'",
                    ENTITLED_VOUCHERS_NUMBER_ELEM_ID, order.getId()));
        }
        if (!elementMap.containsKey(ENTITLED_PLN_AMOUNT_ELEM_ID)){
            throw new RuntimeException(String.format("Błąd konfiguracji - nie istnieje pole w '%s' dla zamówienia '%s'",
                    ENTITLED_PLN_AMOUNT_ELEM_ID, order.getId()));
        }
        if (!elementMap.containsKey(LIMIT_EUR_AMOUNT_ELEM_ID)){
            throw new RuntimeException(String.format("Błąd konfiguracji - nie istnieje pole w '%s' dla zamówienia '%s'",
                    LIMIT_EUR_AMOUNT_ELEM_ID, order.getId()));
        }
        if (!elementMap.containsKey(EXCHANGE_ELEM_ID)){
            throw new RuntimeException(String.format("Błąd konfiguracji - nie istnieje pole w '%s' dla zamówienia '%s'",
                    EXCHANGE_ELEM_ID, order.getId()));
        }
    }

    /**
     * Uaktulnia konkratny elemnty składowych complext typ'a
     * @param order zamówienie
     * @param elementMap mapa z elementami - klucz ELEMENT_D, obiekt - OrderElement
     * @param elementId identyfilator elementu który ma być uaktualniony
     * @param num nowa wartość
     */
    private void updateElement(Order order, Map<String, OrderElement> elementMap, String elementId, BigDecimal num){
        OrderElement simpleElement = elementMap.get(elementId);
        if (simpleElement == null){
            throw new RuntimeException(String.format("Błąd konfiguracji - nie istnieje pole w '%s' dla zamówienia '%s'",
                    elementId, order.getId()));
        }
        simpleElement.setValueNumber(num);
    }

 /**
     * Metoda wyszukuje obiekt GrantProgramLimit na podstawie id programu dofinansowania i innych właściwości limitu . Metoda sprawdza czy w bazie znajduje się dokałdnie
     * jedna instancja obiektu GrantProgramLimit dla zadanych parametrów.
     *  
     */
    private GrantProgramLimit findGrantProgramLimit(Long grantProgramId, String enterpriseSizeId, GrantProgramLimit.LimitType limitType, Date date){
        List<GrantProgramLimit> GrantProgramLimits = grantProgramLimitRepository.findByGrantProgramEntSizeLimitTypeInDate(grantProgramId, enterpriseSizeId, limitType, date);
        if(GrantProgramLimits.size() == 0){
            throw new RuntimeException(String.format("Błąd parmetryzacji w tabeli APP_PBE.GRANT_PROGRAM_LIMITS. Nie znaleziono żadnego limitu typu [%s] dla rozmiaru przedsiębiorstwa [%s] dla programu [%s] obowiązującego dnia [%s]",limitType, enterpriseSizeId, grantProgramId, date));
        }
        if(GrantProgramLimits.size() > 1){
            throw new RuntimeException(String.format("Błąd parmetryzacji w tabeli APP_PBE.GRANT_PROGRAM_LIMITS. Dla danego programu [%s] znaleziono więcej niż jeden limit typu [%s] dla rozmiaru przedsiębiorstwa [%s] obowiązujący dnia [%s]", grantProgramId,limitType, enterpriseSizeId, date));
        }
        return GrantProgramLimits.get(0);
    }        

    /**
     * Funkcja zwraca przysługującą w danym momencie MŚP liczbę bonów biorąc pod uwagę wykorzystane limity oraz liczbę wnioskowaną bonów
     * @param order - obiekt zamówienia bonów
     * @param application obiekt wniosku o dofinansowanie
     * @return przysługująca liczba bonów
     */
    private Long getEntitledVouchersNumber(Order order, GrantApplication application) {
        GrantProgramLimit limit =  findGrantProgramLimit(application.getProgram().getId(),
                                         application.getBasicData().getEnterpriseSize().getId(),
                                         GrantProgramLimit.LimitType.ORDVOULIM, 
                                         order.getOrderDate());
        Long grantedVoucherNumber = orderRepository.findGrantedVoucherNumberForEntAndProgram(application.getEnterprise().getId(),application.getProgram().getId());
        Long remainingLimit = limit.getLimitValue().intValue() - grantedVoucherNumber;
        if (remainingLimit<=0) {
            return 0L;
        } 
        return remainingLimit;
    }
}
