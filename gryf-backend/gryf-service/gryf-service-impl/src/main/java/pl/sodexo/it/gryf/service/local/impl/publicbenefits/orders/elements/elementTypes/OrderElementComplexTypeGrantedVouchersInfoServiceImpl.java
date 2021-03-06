package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.elementTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementComplexTypeGrantedVouchersInfoDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderElementRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderRepository;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramLimit;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.service.local.api.ParamInDateService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.elementTypes.OrderElementComplexTypeGrantedVouchersInfoService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.orderflows.OrderFlowElementService;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.OrderElementBaseService;
import pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.orders.action.OrderElementDTOProvider;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by tomasz.bilski.ext on 2015-08-27.
 */
@Service("orderElementComplexTypeGrantedVouchersInfoService")
public class OrderElementComplexTypeGrantedVouchersInfoServiceImpl extends OrderElementBaseService<OrderElementComplexTypeGrantedVouchersInfoDTO>
        implements OrderElementComplexTypeGrantedVouchersInfoService {


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
    private OrderFlowElementService orderFlowElementService;
        
    @Autowired
    private OrderElementRepository orderElementRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ParamInDateService paramInDateService;

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

        return OrderElementDTOProvider.createOrderElementComplexTypeGrantedVouchersInfoDTO(builder, entitledVouchersNumber,
                                                        entitledPlnAmount, limitEurAmount, exchange);
    }

    @Override
    public void updateValue(OrderElement element, OrderElementComplexTypeGrantedVouchersInfoDTO dto) {

        Order order = element.getOrder();
        Map<String, OrderElement> elementMap = orderElementRepository.findByOrderAndElements(order.getId(), LIST_ELEM_ID);

        updateElement(order, elementMap, ENTITLED_VOUCHERS_NUMBER_ELEM_ID, (dto.getEntitledVouchersNumber() != null) ?
                BigDecimal.valueOf(dto.getEntitledVouchersNumber()) : null);
        updateElement(order, elementMap, ENTITLED_PLN_AMOUNT_ELEM_ID, dto.getEntitledPlnAmount());
        updateElement(order, elementMap, LIMIT_EUR_AMOUNT_ELEM_ID, dto.getLimitEurAmount());
        updateElement(order, elementMap, EXCHANGE_ELEM_ID, dto.getExchange());
        order.setVouchersNumber(dto.getGivenVouchersNumber());

        element.setCompletedDate(new Date());
    }

    @Override
    public void addVouchersInfoElements(Order order){
        GrantApplication application = order.getApplication();

        /* wyznacz przysługującą ilość bonów dla przedsiębiorstwa */
        Long entitledVouchNumber = getEntitledVouchersNumber(order, application);

        orderFlowElementService.addElementNumberValue(order, ENTITLED_VOUCHERS_NUMBER_ELEM_ID, BigDecimal.valueOf(entitledVouchNumber));
        orderFlowElementService.addElementNumberValue(order, ENTITLED_PLN_AMOUNT_ELEM_ID,  order.getProduct().getPbeAidValue().multiply(BigDecimal.valueOf(entitledVouchNumber)));
        orderFlowElementService.addElementNumberValue(order, LIMIT_EUR_AMOUNT_ELEM_ID, null);
        orderFlowElementService.addElementNumberValue(order, EXCHANGE_ELEM_ID, null);
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
     * Funkcja zwraca przysługującą w danym momencie MŚP liczbę bonów biorąc pod uwagę wykorzystane limity oraz liczbę wnioskowaną bonów
     * @param order - obiekt zamówienia bonów
     * @param application obiekt wniosku o dofinansowanie
     * @return przysługująca liczba bonów
     */
    private Long getEntitledVouchersNumber(Order order, GrantApplication application) {
        GrantProgramLimit limit =  paramInDateService.findGrantProgramLimit(application.getProgram().getId(),
                                         application.getBasicData().getEnterpriseSize().getId(),
                                         GrantProgramLimit.LimitType.ORDVOULIM, 
                                         order.getOrderDate(), true);
        Long grantedVoucherNumber = orderRepository.findGrantedVoucherNumberForEntAndProgram(application.getEnterprise().getId(),application.getProgram().getId());
        Long remainingLimit = limit.getLimitValue().intValue() - grantedVoucherNumber;
        if (remainingLimit<=0) {
            return 0L;
        } 
        return remainingLimit;
    }
}
