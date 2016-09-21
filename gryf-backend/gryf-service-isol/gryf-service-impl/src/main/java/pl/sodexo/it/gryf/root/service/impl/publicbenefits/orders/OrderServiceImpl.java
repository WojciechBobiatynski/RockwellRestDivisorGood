package pl.sodexo.it.gryf.root.service.impl.publicbenefits.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.OrderDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.action.IncomingOrderElementDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementAttachmentDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.transitions.OrderFlowTransitionDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.searchform.OrderSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.searchform.OrderSearchResultDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.exception.InvalidObjectIdException;
import pl.sodexo.it.gryf.common.exception.StaleDataException;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.common.utils.JsonMapperUtils;
import pl.sodexo.it.gryf.common.utils.LoginUtils;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationVersion;
import pl.sodexo.it.gryf.model.publicbenefits.orders.*;
import pl.sodexo.it.gryf.root.repository.GryfPLSQLRepository;
import pl.sodexo.it.gryf.root.repository.publicbenefits.orders.*;
import pl.sodexo.it.gryf.root.service.SecurityCheckerService;
import pl.sodexo.it.gryf.root.service.local.FileService;
import pl.sodexo.it.gryf.root.service.local.ValidateService;
import pl.sodexo.it.gryf.root.service.local.publicbenefits.orders.actions.ActionService;
import pl.sodexo.it.gryf.root.service.local.publicbenefits.orders.elements.OrderElementService;
import pl.sodexo.it.gryf.root.service.local.publicbenefits.orders.orderflows.OrderFlowService;
import pl.sodexo.it.gryf.root.service.publicbenefits.orders.OrderService;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-22.
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    //PRIVATE FIELDS

    @Autowired
    private ApplicationContext context;

    @Autowired
    private SecurityCheckerService securityCheckerService;

    @Autowired
    private ValidateService validateService;

    @Autowired
    private FileService fileService;

    @Autowired
    private OrderElementRepository orderElementRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderFlowStatusTransitionRepository orderFlowStatusTransitionRepository;

    @Autowired
    private OrderFlowRepository orderFlowRepository;

    @Autowired
    private OrderFlowStatusTransSqlRepository orderFlowStatusTransSqlRepository;

    @Autowired
    private OrderFlowAllowedDelayRepository orderFlowAllowedDelayRepository;

    @Autowired 
    private GryfPLSQLRepository gryfPLSQLRepository;
    
    @Autowired
    private OrderFlowElementInStatusRepository orderFlowElementInStatusRepository;
    
    @Autowired
    private OrderFlowElementRepository orderFlowElementRepository;
    
    //PUBLIC METHODS

    @Override
    public String getOrderDataToModify(Long id) {

        Order order = orderRepository.get(id);
        if (order == null){
            throw new InvalidObjectIdException("Nie znaleziono zamówienia o id " + id);
        }
        List<OrderElementDTOBuilder> orderElementDTOBuilders = orderElementRepository.findDtoFactoryByOrderToModify(id);
        List<OrderFlowTransitionDTO> orderFlowStatusTransitions = orderFlowStatusTransitionRepository.findDtoByOrder(id);

        OrderDTO orderDTO = createOrderDTO(order, orderElementDTOBuilders, orderFlowStatusTransitions);
        return JsonMapperUtils.writeValueAsString(orderDTO);
    }

    @Override
    public String getOrderDataToPreview(Long id) {

        Order order = orderRepository.get(id);
        if (order == null){
            throw new InvalidObjectIdException("Nie znaleziono zamówienia o id " + id);
        }        
        List<OrderElementDTOBuilder> orderElementDTOBuilders = orderElementRepository.findDtoFactoryByOrderToPreview(id);
        List<OrderFlowTransitionDTO> orderFlowStatusTransitions = new ArrayList<>();

        OrderDTO orderDTO = createOrderDTO(order, orderElementDTOBuilders, orderFlowStatusTransitions);
        return JsonMapperUtils.writeValueAsString(orderDTO);
    }

    @Override
    public List<OrderSearchResultDTO> findOrders(OrderSearchQueryDTO searchDTO) {
        List<Object[]> orders = orderRepository.findOrders(searchDTO);
        return OrderSearchResultDTO.createList(orders);
    }

    @Override
    public Order createOrder(GrantApplication grantApplication){

        GrantApplicationVersion grantApplicationVersion = grantApplication.getApplicationVersion();
        OrderFlow orderFlow = findOrderFlow(grantApplicationVersion.getId());

        OrderFlowService orderFlowService = (OrderFlowService)GryfUtils.findBean(context, orderFlow.getServiceBeanName());
        Order order = orderFlowService.createOrder(grantApplication, orderFlow);
        addElementsByOrderStatus(order);
        return order;
    }

    @Override
    public void executeAction(Long id, Long actionId, Integer version, List<IncomingOrderElementDTO> incomingOrderElements, List<FileDTO> files, List<String> acceptedViolations) {

        try {

            //ORDER STUFF
            Order order = orderRepository.get(id);
            OrderFlow orderFlow = order.getOrderFlow();
            OrderFlowStatus status = order.getStatus();

            //VALIDATE VERSION
            validateVersion(order, version);

            //STATUS TRANSITION
            OrderFlowStatusTransitionPK statusTransitionId = new OrderFlowStatusTransitionPK(orderFlow.getId(), status.getStatusId(), actionId);
            OrderFlowStatusTransition statusTransition = orderFlowStatusTransitionRepository.get(statusTransitionId);

            //UPRAWNINIE DO AKCJI
            validateActionPrivilege(statusTransition);

            //CREATE ORDER ELEMENT DTO
            List<OrderElementDTO> elementDtoList = createElementDtoList(incomingOrderElements, files);

            //VALIDACJA ELEMENTOW
            validateElements(order, elementDtoList);

            //UPDATE ELEMENTS
            updateElements(order, elementDtoList);

            //EXECUTE PRE SQL
            executeSql(order, statusTransition, OrderFlowStatusTransSqlType.PRE);

            //EXECUTE ACTION
            if (!StringUtils.isEmpty(statusTransition.getActionBeanName())) {
                ActionService actionService = (ActionService) GryfUtils.findBean(context, statusTransition.getActionBeanName());
                actionService.execute(order, acceptedViolations);
            }

            //EXECUTE POST SQL
            executeSql(order, statusTransition, OrderFlowStatusTransSqlType.POST);

            //SET STATUS
            order.setStatus(statusTransition.getNextStatus());
            addElementsByOrderStatus(order);
            
            //FILL REQUIRED DATE FOR LAZY FIELDS
            fillRequiredDateForLazyFields(order);

        //KASUJEMY PLIKI
        }catch(RuntimeException e){
            OrderService orderService = context.getBean(OrderServiceImpl.class);
            fileService.deleteSavedFiles(files);
            throw e;
        }
    }

    @Override
    public FileDTO getOrderAttachmentFile(Long elementId) {
        OrderElement orderElement = orderElementRepository.get(elementId);
        FileDTO dto = new FileDTO();
        dto.setName(StringUtils.findFileNameInPath(orderElement.getValueVarchar()));
        dto.setInputStream(fileService.getInputStream(orderElement.getValueVarchar()));
        return dto;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void manageLocking(Long id) {
        Order order = orderRepository.get(id);
        throw new StaleDataException(order.getId(), order);
    }

    @Override
    public void addElementVarcharValue(Order order, final String elementId, String valueVarchar){
        addElementWithValue(order, elementId, null, valueVarchar, null);
    }

    @Override
    public void addElementNumberValue(Order order, final String elementId, BigDecimal valueNumber){
        addElementWithValue(order, elementId, valueNumber, null, null);
    }
    
    @Override
    public void addElementWithValue(Order order, final String elementId, BigDecimal valueNumber, String valueVarchar, Date valueDate){
        boolean contain = GryfUtils.contain(order.getOrderElements(), new GryfUtils.Predicate<OrderElement>() {
            public boolean apply(OrderElement input) {
                return input.getOrderFlowElement().getElementId().equals(elementId);
            }
        });
        if(!contain) {
            OrderFlowElement ofe = orderFlowElementRepository.get(elementId);
            if(ofe == null){
                throw new RuntimeException(String.format("Błąd konfiguracji - " +
                                "nie znaleziono OrderFlowElement dla id '%s'", elementId));
            }
            OrderElement e = new OrderElement();
            e.setOrderFlowElement(ofe);
            if (valueNumber != null){
                e.setValueNumber(valueNumber);
            }
            if (valueVarchar != null){
                e.setValueVarchar(valueVarchar);            
            }
            if (valueDate != null){
                e.setValueDate(valueDate);            
            }
            order.addOrderElement(e);
        }
    }    
    
    @Override
    public BigDecimal getPaymentAmount(Order order){
        if (order == null)
            return null;
        try { 
            return order.getProduct().getPbeTotalValue().subtract(order.getProduct().getPbeAidValue()).multiply(new BigDecimal(order.getVouchersNumber()));
        } catch (Exception e) {
            throw new RuntimeException("Nie udało się wyznaczyć kwoty płatności dla przedsiębiorstwa, prawdopodobnie brakujące dane na zamówieniu/produkcie lub niewłaściwa parametryzacja.");
        }
    }
    
    //PRIVATE METHODS
    /**
     * Metoda tworzy liste z obiektami OrderElementDTO. Na podstawie pola IncomingOrderElementDTO.elementTypeComponentName wiemy na jaką klase parsować
 pole IncomingOrderElementDTO.data. Po sprarsowaniu dodatkow doczepiamy pliki jeżeli zostal plik dodany (ustawione pole fileIncluded).
     * @param incomingOrderElements lista z typem componentu oraz danymi do parsowania
     * @param files pliki zalacznikow
     * @return stworzona lista obiektow typu OrderElementDTO
     */
    private List<OrderElementDTO> createElementDtoList(List<IncomingOrderElementDTO> incomingOrderElements, List<FileDTO> files){
        int index = 0;
        List<OrderElementDTO> elementDtoList = new ArrayList<>();
        for (IncomingOrderElementDTO o : incomingOrderElements) {

            //PARSOWANIE
            String dtoClassName = OrderFlowElementType.getDtoClassName(o.getElementTypeComponentName());
            OrderElementDTO orderElement = OrderParser.readOrderElement(dtoClassName, o.getData());

            //DOPIECEI ZALACZNIKOW
            if(orderElement instanceof OrderElementAttachmentDTO){
                OrderElementAttachmentDTO orderElementAttachment = (OrderElementAttachmentDTO)orderElement;
                if(orderElementAttachment.isFileIncluded()){
                    orderElementAttachment.setFile(files.get(index++));
                    orderElementAttachment.getFile().setAttachmentName(orderElementAttachment.getName());
                }
            }

            elementDtoList.add(orderElement);
        }
        return elementDtoList;
    }

    //PROTECTED METHODS

    /**
     * Tworzy obiekt OrderDTO
     * @param order zamówienia
     * @param orderElementDTOBuilders lista obiektów OrderElementDTOBuilder
     * @param orderFlowStatusTransitions lista akcji
     * @return obiekt reprezentujący zamówienie
     */
    private OrderDTO createOrderDTO(Order order, List<OrderElementDTOBuilder> orderElementDTOBuilders, List<OrderFlowTransitionDTO> orderFlowStatusTransitions){
        List<OrderElementDTO> elements = new ArrayList<>();
        for (OrderElementDTOBuilder builder : orderElementDTOBuilders) {
            OrderFlowElement ofe = builder.getOrderFlowElement();
            OrderFlowElementType ofet = ofe.getOrderFlowElementType();

            OrderElementService service = (OrderElementService)GryfUtils.findBean(context, ofet.getServiceBeanName());
            elements.add(service.createElement(builder));
        }

        return OrderDTO.create(order.getId(), elements, orderFlowStatusTransitions, order.getVersion());
    }

    private void validateVersion(Order order, Integer version){
        if(!Objects.equals(order.getVersion(), version)){
            throw new StaleDataException(order.getId(), order);
        }
    }

    /**
     * Dodaje elementy do danego zamówienia. Elementy dodawane są na podstawie list elementów w danym statusie (w statusie
     * danego zamówienia).
     * @param order zamowienie
     */
    private void addElementsByOrderStatus(Order order){
        List<OrderFlowElementInStatus> elementsInStatus = order.getStatus().getOrderFlowElementsInStatus();
        for (final OrderFlowElementInStatus eis : elementsInStatus) {

            boolean contain = GryfUtils.contain(order.getOrderElements(), new GryfUtils.Predicate<OrderElement>() {
                public boolean apply(OrderElement input) {
                    return input.getOrderFlowElement().equals(eis.getOrderFlowElement());
                }
            });
            if(!contain) {
                OrderElement e = new OrderElement();
                if (eis.getOrderFlowAllowedDelayType() != null){
                    OrderFlowAllowedDelay orderFlowAllowedDelayForElement = findOrderFlowAllowedDelay(order.getOrderFlow().getId(), eis.getOrderFlowAllowedDelayType().getId());
                    e.setRequiredDate(calculateRequiredDate(orderFlowAllowedDelayForElement, order));
                }
                e.setOrderFlowElement(eis.getOrderFlowElement());
                order.addOrderElement(e);                
            }
        }
    }

    /**
     * Sprawdza uprawnine do wykoninia akcji.
     * @param statusTransition obiekt reprezentujacy przejscie pomiedzy akcjiami
     */
    private void validateActionPrivilege(OrderFlowStatusTransition statusTransition) {
        String privilege = statusTransition.getAugIdRequired();
        if(!StringUtils.isEmpty(privilege)){
            if(!securityCheckerService.hasPrivilege(privilege)){
                validateService.validate("Nie posiadasz uprawnień do wykonania danej akcji");
            }
        }
    }

    /**
     * Waliduje elementy. Dla elementów wywołuje metode validacyjną w zależnosci o typu elementu.
     * @param order zamowienie
     * @param elementDtoList lista obiektów dto
     */
    private void validateElements(Order order, List<OrderElementDTO> elementDtoList){
        List<EntityConstraintViolation> violations = new ArrayList<>();
        OrderFlowStatus status = order.getStatus();

        //MAPA - klucz: identyfikator OrderFlowElementInStatus, wartość: objekt OrderFlowElementInStatus
        Map<String, OrderFlowElementInStatus> ofeisMap = GryfUtils.constructMap(status.getOrderFlowElementsInStatus(),
                new GryfUtils.MapConstructor<String, OrderFlowElementInStatus>() {
                    public boolean isAddToMap(OrderFlowElementInStatus input) {
                        return true;
                    }

                    public String getKey(OrderFlowElementInStatus input) {
                        return input.getId().getElementId();
                    }
                });

        //MAPA - klucz: identyfikator OrderElement, wartość: objekt OrderElement
        Map<Long, OrderElement> oeMap = GryfUtils.constructMap(order.getOrderElements(),
                new GryfUtils.MapConstructor<Long, OrderElement>() {
                    public boolean isAddToMap(OrderElement input) {
                        return true;
                    }
                    public Long getKey(OrderElement input) {
                        return input.getId();
                    }
                });

        //LISTA PO DTO (DLA KAŻDEGO ODPALANEI WALIDACJI)
        for (OrderElementDTO elementDTO : elementDtoList) {
            String serviceName = OrderFlowElementType.getServiceBeanName(elementDTO.getElementTypeComponentName());
            OrderElementService service = (OrderElementService)GryfUtils.findBean(context,  serviceName);

            //POBRANIE "ELEMENTU W STATUSIE" DLA DANEGO DTO
            OrderFlowElementInStatus ofeis = ofeisMap.get(elementDTO.getOrderFlowElementId());
            if(ofeis == null){
                throw new RuntimeException(String.format("Bład konfiguracji - element '%s' nie może znajdowac się w statusie '%s'",
                                                            elementDTO.getName(), status.getStatusName()));
            }

            //POBRANIE "ELEMENTU" DLA DANEGO DTO
            OrderElement oe = oeMap.get(elementDTO.getId());
            if(ofeis == null){
                throw new RuntimeException(String.format("Bład konfiguracji - element '%s' nie został dodany do zamówienia (id zamowienia - '%s')",
                        elementDTO.getName(), oe.getId()));
            }

            //WALIDACJA
            service.validate(violations, oe, ofeis, elementDTO);
        }

        //VALIDATE
        validateService.validate(violations);
    }

    /**
     * Metoda aktualizuje elementy zamówienia na podstawie listy objektów dto. Poszczególne
     * elementy aktualizowane są przez konretne serwisy, które odpowiadaja typom elementów.
     * @param order zamowenie
     * @param elementDtoList lista dto
     */
    private void updateElements(Order order, List<OrderElementDTO> elementDtoList){
        for (final OrderElement element : order.getOrderElements()) {
            OrderFlowElement ofe = element.getOrderFlowElement();
            OrderFlowElementType ofet = ofe.getOrderFlowElementType();

            //DTO DLA DANEGO ELEMENT
            OrderElementDTO dto = GryfUtils.find(elementDtoList, new GryfUtils.Predicate<OrderElementDTO>() {
                public boolean apply(OrderElementDTO input) {
                    return input.getId().equals(element.getId());
                }
            });

            //AKTUALIZACJIA WARTOSCI
            if(dto != null){
                OrderElementService orderElementService = (OrderElementService)GryfUtils.findBean(context, ofet.getServiceBeanName());
                orderElementService.updateValue(element, dto);
            }
        }
    }

    /**
     * Wykonywany jest sql, dopiey do konkretnego przejścia między statusami.
     * @param order zamówienie
     * @param statusTransition przejsćie pomiędzy statusami
     * @param type typ sql PRE lub POST
     */
    private void executeSql(Order order, OrderFlowStatusTransition statusTransition, OrderFlowStatusTransSqlType type) {
        List<OrderFlowStatusTransSql> transSqlList = statusTransition.getOrderFlowStatusTransSqlList();
        for (OrderFlowStatusTransSql transSql : transSqlList) {
            if(type == transSql.getType()){
                orderFlowStatusTransSqlRepository.flush();
                orderFlowStatusTransSqlRepository.executeNativeSql(transSql, order.getId(), LoginUtils.getLogin());
            }
        }
    }

    /**
     * Metoda wyszukuje objekt OrderFlow na podstawie id wersji wniosku. Metoda sprawdza czy w bazie znajduje się dokałdnie
     * jedna instancja objetu OrderFlow dla danej wersji. Jeżeli nie bedzie znajdować sietylko jedna instancja zostanie wygenerowany wyjatek.
     * @param id id wersji aplikacji
     * @return objekt typu OrderFlow
     */
    private OrderFlow findOrderFlow(Long id){
        List<OrderFlow> orderFlows = orderFlowRepository.findByGrantApplicationVersionInDate(id, new Date());
        if(orderFlows.size() == 0){
            throw new RuntimeException(String.format("Nie znaleziono żadnego order flow dla wersji [%s]",id));
        }
        if(orderFlows.size() > 1){
            throw new RuntimeException(String.format("Dla danej wersji wniosku [%s] znaleziono wiecej niż jeden order flow", id));
        }
        return orderFlows.get(0);
    }
    
    /**
     * Metoda wyszukuje obiekt OrderFlowAllowedDelay na podstawie id przepływu zamówienia i typu dopuszczalnego opóźnienia. Metoda sprawdza czy w bazie znajduje się dokałdnie
     * jedna instancja obiektu OrderFlowAllowedDelay dla zadanych parametrów. .
     * @param orderFlowId id przepływu zamówienia
     * @param delayTypeId id typu opóźnienia 
     * @return objekt typu OrderFlowAllowedDelay
     */
    private OrderFlowAllowedDelay findOrderFlowAllowedDelay(Long orderFlowId, String delayTypeId){
        List<OrderFlowAllowedDelay> orderFlowAllowedDelays = orderFlowAllowedDelayRepository.findByOrderFlowAndDelayType(orderFlowId, delayTypeId);
        if(orderFlowAllowedDelays.size() == 0){
            throw new RuntimeException(String.format("Błąd parmetryzacji w tabeli APP_PBE.ORDER_FLOW_ALLOWED_DELAYS. Nie znaleziono żadnego dopuszczalnego opóźnienia typu [%s] dla order flow dla wersji [%s]", delayTypeId, orderFlowId));
        }
        if(orderFlowAllowedDelays.size() > 1){
            throw new RuntimeException(String.format("Błąd parmetryzacji w tabeli APP_PBE.ORDER_FLOW_ALLOWED_DELAYS. Dla danej wersji order flow [%s] znaleziono więcej niż jedno opuźnienie typu [%s]", orderFlowId, delayTypeId));
        }
        return orderFlowAllowedDelays.get(0);
    }
    
    /**
     * Metoda wyznacza datę wymaganą uzupełnienia elementu zamówienia na podstawie dopuszczalnego opóźnienia i obiektu zamówienia
     * @param orderFlowAllowedDelay Dopuszczalny typ opóźnienia
     * @param order Zamówienie
     * @return wyznaczona data wymagana wypełnienia pola
     */
    private Date calculateRequiredDate(OrderFlowAllowedDelay orderFlowAllowedDelay, Order order){
        //determine delay start date
        Date delayStartDate = null;
        switch (orderFlowAllowedDelay.getDelayType().getDelayStartingPointType().getId()) {
            case SYSDATE: 
                delayStartDate = new Date();
                break;
            case ORDERDATE: 
                delayStartDate = order.getOrderDate();
                break;
            case SIGNDATE:
                throw new UnsupportedOperationException("Typ daty początkowej opóźnienia " + orderFlowAllowedDelay.getDelayType().getDelayStartingPointType().getId().getLabel() + " nieobsługiwany");
            case CONTRDATE: 
                throw new UnsupportedOperationException("Typ daty początkowej opóźnienia " + orderFlowAllowedDelay.getDelayType().getDelayStartingPointType().getId().getLabel() + " nieobsługiwany");
            case EXEDATE: 
                throw new UnsupportedOperationException("Typ daty początkowej opóźnienia " + orderFlowAllowedDelay.getDelayType().getDelayStartingPointType().getId().getLabel() + " nieobsługiwany");
            default: 
                throw new UnsupportedOperationException("Typ daty początkowej opóźnienia " + orderFlowAllowedDelay.getDelayType().getDelayStartingPointType().getId().getLabel() + " nieobsługiwany");
        }
        return gryfPLSQLRepository.getNthDay(delayStartDate, orderFlowAllowedDelay.getDelayValue(), orderFlowAllowedDelay.getDelayDaysType());
    }
    
 /**
     * Aktualizuje daty wymagane uzupełnienia danych dla tych elementów zamówienia, dla których nie było takiej możliwości wcześniej
     * @param order zamowienie
     */
    private void fillRequiredDateForLazyFields(Order order){
        for (final OrderElement oe : order.getOrderElements()) {
            // instancja elementu na zamówieniu nie ma uzupełnionej daty wymaganej
            if (oe.getRequiredDate() == null){
                OrderFlowElementInStatus orderFlowElementInStatus = findOrderFlowElementInStatus(order.getStatus().getStatusId(), oe.getOrderFlowElement().getElementId());
                //element jest widoczny w aktualnym statusie zamówienia
                if (orderFlowElementInStatus != null) {
                    OrderFlowAllowedDelayType orderFlowAllowedDelayType = orderFlowElementInStatus.getOrderFlowAllowedDelayType();
                    // element w danym statusie ma w definicji datę wymaganą
                    if (orderFlowAllowedDelayType != null){
                        OrderFlowAllowedDelay orderFlowAllowedDelayForElement = findOrderFlowAllowedDelay(order.getOrderFlow().getId(), orderFlowAllowedDelayType.getId());
                        oe.setRequiredDate(calculateRequiredDate(orderFlowAllowedDelayForElement, order));
                    }
                }
            }
        }
    }    
    
    /**
     * Metoda wyszukuje obiekt OrderFlowElementInStatus na podstawie id statusu i id elementu. Metoda sprawdza czy w bazie znajduje się dokałdnie
     * jedna instancja obiektu OrderFlowElementInStatus dla zadanych parametrów.
     * @param orderStatusId id statusu zamówienia
     * @param elementId id elementu 
     * @return objekt typu OrderFlowElementInStatus
     */
    private OrderFlowElementInStatus findOrderFlowElementInStatus(String orderStatusId, String elementId){
        List<OrderFlowElementInStatus> orderFlowElementsInStatus = orderFlowElementInStatusRepository.findByOrderStatusIdAndElementId(orderStatusId, elementId);
        if(orderFlowElementsInStatus.size() > 1){
            throw new RuntimeException(String.format("Błąd parmetryzacji w tabeli APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS. Znaleziono więcej niż jeden element [%s] dla statusu [%s]", elementId, orderStatusId));
        }
        if (orderFlowElementsInStatus.size() == 1){
            return orderFlowElementsInStatus.get(0);
        }
        return null;
    }    
    
}
