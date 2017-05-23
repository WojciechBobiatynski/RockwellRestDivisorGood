package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.orderflows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.action.IncomingOrderElementDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementAttachmentDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.parsers.OrderParser;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderFlowElementRepository;
import pl.sodexo.it.gryf.model.publicbenefits.orders.*;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.OrderDateService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.OrderElementService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.orderflows.OrderFlowElementService;
import pl.sodexo.it.gryf.service.utils.BeanUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by jbentyn on 2016-09-28.
 */
@Service
@Transactional
public class OrderFlowElementServiceImpl implements OrderFlowElementService {

    @Autowired
    private OrderFlowElementRepository orderFlowElementRepository;

    @Autowired
    private OrderDateService orderDateService;

    @Autowired
    private GryfValidator gryfValidator;

    @Autowired
    private ApplicationContext context;

    @Override
    public void addElementsByOrderStatus(Order order) {
        List<OrderFlowElementInStatus> elementsInStatus = order.getStatus().getOrderFlowElementsInStatus();
        for (final OrderFlowElementInStatus eis : elementsInStatus) {

            boolean contain = GryfUtils.contain(order.getOrderElements(), new GryfUtils.Predicate<OrderElement>() {

                public boolean apply(OrderElement input) {
                    return input.getOrderFlowElement().equals(eis.getOrderFlowElement());
                }
            });
            if (!contain) {
                OrderElement e = new OrderElement();
                if (eis.getOrderFlowAllowedDelayType() != null) {
                    OrderFlowAllowedDelay orderFlowAllowedDelayForElement = orderDateService.findOrderFlowAllowedDelay(order.getOrderFlow().getId(), eis.getOrderFlowAllowedDelayType().getId());
                    e.setRequiredDate(orderDateService.calculateRequiredDate(orderFlowAllowedDelayForElement, order));
                }
                e.setOrderFlowElement(eis.getOrderFlowElement());
                order.addOrderElement(e);
            }
        }
    }

    @Override
    public void addElementVarcharValue(Order order, final String elementId, String valueVarchar) {
        addElementWithValue(order, elementId, null, valueVarchar, null);
    }

    @Override
    public void addElementCheckboxValue(Order order, final String elementId, boolean valueBoolean){
        addElementWithValue(order, elementId, null, Boolean.toString(valueBoolean), null);
    }

    @Override
    public void addElementNumberValue(Order order, final String elementId, BigDecimal valueNumber) {
        addElementWithValue(order, elementId, valueNumber, null, null);
    }

    @Override
    public void addElementEmpty(Order order, final String elementId){
        addElementWithValue(order, elementId, null, null, null);
    }

    private void addElementWithValue(Order order, final String elementId, BigDecimal valueNumber, String valueVarchar, Date valueDate) {
        boolean contain = GryfUtils.contain(order.getOrderElements(), new GryfUtils.Predicate<OrderElement>() {
            public boolean apply(OrderElement input) {
                return input.getOrderFlowElement().getElementId().equals(elementId);
            }
        });
        if (!contain) {

            //USTAWIENIE WARTSCI
            OrderFlowElement ofe = orderFlowElementRepository.get(elementId);
            if (ofe == null) {
                throw new RuntimeException(String.format("Błąd konfiguracji - " + "nie znaleziono OrderFlowElement dla id '%s'", elementId));
            }
            OrderElement e = new OrderElement();
            e.setOrderFlowElement(ofe);
            if (valueNumber != null) {
                e.setValueNumber(valueNumber);
            }
            if (valueVarchar != null) {
                e.setValueVarchar(valueVarchar);
            }
            if (valueDate != null) {
                e.setValueDate(valueDate);
            }

            //USTAWIENIE DELAY
            List<OrderFlowElementInStatus> elementsInStatus = order.getStatus().getOrderFlowElementsInStatus();
            OrderFlowElementInStatus eis = GryfUtils.find(elementsInStatus, new GryfUtils.Predicate<OrderFlowElementInStatus>() {
                public boolean apply(OrderFlowElementInStatus input) {
                    return input.getOrderFlowElement().equals(ofe);
                }
            });
            if(eis != null){
                if (eis.getOrderFlowAllowedDelayType() != null) {
                    OrderFlowAllowedDelay orderFlowAllowedDelayForElement = orderDateService.findOrderFlowAllowedDelay(order.getOrderFlow().getId(), eis.getOrderFlowAllowedDelayType().getId());
                    e.setRequiredDate(orderDateService.calculateRequiredDate(orderFlowAllowedDelayForElement, order));
                }
            }

            order.addOrderElement(e);
        }
    }

    @Override
    public List<OrderElementDTO> createElementDtoList(List<IncomingOrderElementDTO> incomingOrderElements, List<FileDTO> files) {
        int index = 0;
        List<OrderElementDTO> elementDtoList = new ArrayList<>();
        for (IncomingOrderElementDTO o : incomingOrderElements) {

            //PARSOWANIE
            String dtoClassName = OrderFlowElementType.getDtoClassName(o.getElementTypeComponentName());
            OrderElementDTO orderElement = OrderParser.readOrderElement(dtoClassName, o.getData());

            //DOPIECEI ZALACZNIKOW
            if (orderElement instanceof OrderElementAttachmentDTO) {
                OrderElementAttachmentDTO orderElementAttachment = (OrderElementAttachmentDTO) orderElement;
                if (orderElementAttachment.isFileIncluded()) {
                    orderElementAttachment.setFile(files.get(index++));
                    orderElementAttachment.getFile().setAttachmentName(orderElementAttachment.getName());
                }
            }

            elementDtoList.add(orderElement);
        }
        return elementDtoList;
    }

    @Override
    public void validateElements(Order order, OrderFlowStatusTransition statusTransition, List<OrderElementDTO> elementDtoList) {
        List<EntityConstraintViolation> violations = new ArrayList<>();
        OrderFlowStatus status = order.getStatus();

        //MAPA - klucz: identyfikator OrderFlowElementInStatus, wartość: objekt OrderFlowElementInStatus
        Map<String, OrderFlowElementInStatus> ofeisMap = GryfUtils.constructMap(status.getOrderFlowElementsInStatus(), new GryfUtils.MapConstructor<String, OrderFlowElementInStatus>() {

            public boolean isAddToMap(OrderFlowElementInStatus input) {
                return true;
            }

            public String getKey(OrderFlowElementInStatus input) {
                return input.getId().getElementId();
            }
        });

        //MAPA - klucz: identyfikator OrderElement, wartość: objekt OrderElement
        Map<Long, OrderElement> oeMap = GryfUtils.constructMap(order.getOrderElements(), new GryfUtils.MapConstructor<Long, OrderElement>() {

            public boolean isAddToMap(OrderElement input) {
                return true;
            }

            public Long getKey(OrderElement input) {
                return input.getId();
            }
        });


        Map<String, OrderFlowStatusTransitionElementFlag> ofeintfMap = GryfUtils.constructMap(statusTransition.getOrderFlowStatusTransitionElementFlags(), new GryfUtils.MapConstructor<String, OrderFlowStatusTransitionElementFlag>() {
            public boolean isAddToMap(OrderFlowStatusTransitionElementFlag input) {
                return true;
            }
            public String getKey(OrderFlowStatusTransitionElementFlag input) {
                return input.getId().getElementId();
            }
        });

        //LISTA PO DTO (DLA KAŻDEGO ODPALANEI WALIDACJI)
        for (OrderElementDTO elementDTO : elementDtoList) {
            String serviceName = OrderFlowElementType.getServiceBeanName(elementDTO.getElementTypeComponentName());
            OrderElementService service = (OrderElementService) BeanUtils.findBean(context, serviceName);

            //POBRANIE "ELEMENTU W STATUSIE" DLA DANEGO DTO
            OrderFlowElementInStatus ofeis = ofeisMap.get(elementDTO.getOrderFlowElementId());
            if (ofeis == null) {
                throw new RuntimeException(String.format("Bład konfiguracji - element '%s' nie może znajdowac się w statusie '%s'", elementDTO.getName(), status.getStatusName()));
            }

            //POBRANIE "ELEMENTU" DLA DANEGO DTO
            OrderElement oe = oeMap.get(elementDTO.getId());
            if (oe == null) {
                throw new RuntimeException(String.format("Bład konfiguracji - element '%s' nie został dodany do zamówienia (id zamowienia - '%s')", elementDTO.getName(), oe.getId()));
            }

            //POBRANIE FLAG ZALEZNYCH OD PRZEJSCIA DLA DANEGO DTO
            OrderFlowStatusTransitionElementFlag ofeintf = ofeintfMap.get(elementDTO.getOrderFlowElementId());

            //WALIDACJA
            service.validate(violations, oe, ofeis, ofeintf, elementDTO);
        }

        //VALIDATE
        gryfValidator.validate(violations);
    }


    @Override
    public void updateElements(Order order, List<OrderElementDTO> elementDtoList) {
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
            if (dto != null) {
                OrderElementService orderElementService = (OrderElementService) BeanUtils.findBean(context, ofet.getServiceBeanName());
                orderElementService.updateValue(element, dto);
            }
        }
    }

}
