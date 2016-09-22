/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.common;

import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowElementType.ElementType;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.elementTypes.OrderElementComplexTypeGrantedVouchersInfoService;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Marcel.GOLUNSKI
 */
public class VerifyOrderHelper {
    
    public static void validateConfirmationCheckboxes(List<EntityConstraintViolation> violations, Order order) {
        for (OrderElement e : order.getOrderElements()) {
            if (e.getOrderFlowElement().getOrderFlowElementType().getId().equals(ElementType.CONFCHKBOX.toString())) {
                if (!Boolean.TRUE.equals(Boolean.valueOf(e.getValueVarchar()))) {
                    violations.add(new EntityConstraintViolation(e.getOrderFlowElement().getElementId(), "Sprawdzenie powinno zostać wykonane a pole wyboru '" + e.getOrderFlowElement().getElementName() +"' zaznaczone" , e.getValueVarchar()));
                }
            }
        }
    }

    public static void validateVouchersNumber(List<EntityConstraintViolation> violations, Order order){
        OrderElement oeLimitEurAmount         = order.getElement(OrderElementComplexTypeGrantedVouchersInfoService.LIMIT_EUR_AMOUNT_ELEM_ID);
        OrderElement oeEntitledPlnAmount      = order.getElement(OrderElementComplexTypeGrantedVouchersInfoService.ENTITLED_PLN_AMOUNT_ELEM_ID);
        OrderElement oeExchange               = order.getElement(OrderElementComplexTypeGrantedVouchersInfoService.EXCHANGE_ELEM_ID);
        OrderElement oeEntitledVouchersNumber = order.getElement(OrderElementComplexTypeGrantedVouchersInfoService.ENTITLED_VOUCHERS_NUMBER_ELEM_ID);

        BigDecimal limitEurAmount         = (oeLimitEurAmount != null && oeLimitEurAmount.getValueNumber() !=null)                 ? oeLimitEurAmount.getValueNumber()         : BigDecimal.ZERO;
        BigDecimal entitledPlnAmount      = (oeEntitledPlnAmount != null && oeEntitledPlnAmount.getValueNumber() !=null)           ? oeEntitledPlnAmount.getValueNumber()      : BigDecimal.ZERO;
        BigDecimal exchange               = (oeExchange != null && oeExchange.getValueNumber() !=null)                             ? oeExchange.getValueNumber()               : BigDecimal.ZERO;
        BigDecimal entitledVouchersNumber = (oeEntitledVouchersNumber != null && oeEntitledVouchersNumber.getValueNumber() !=null) ? oeEntitledVouchersNumber.getValueNumber() : BigDecimal.ZERO;
        BigDecimal voucherAidValue        = (order.getProduct() != null && order.getProduct().getPbeAidValue() != null)            ? order.getProduct().getPbeAidValue()       : BigDecimal.ZERO;

        BigDecimal given;
        if (limitEurAmount.multiply(exchange).compareTo(entitledPlnAmount) >= 0) {
            given = entitledVouchersNumber;
        } else {
            given = limitEurAmount.multiply(exchange).divide(voucherAidValue, 0, BigDecimal.ROUND_FLOOR);
        }

        //PRZYZNANA LICZBA BONÓW - DOBRZE OBLICOZNA
        if(!given.equals(new BigDecimal(order.getVouchersNumber()))){
            violations.add(new EntityConstraintViolation("givenVouchersNumberBadCalculation", String.format("Przyznana liczba %s bonów nie zgadza się z limitem pomocy %s i kursem EUR %s",
                                                                            order.getVouchersNumber(), limitEurAmount, exchange)));
        }

        //PRZYZNANA LICZBA BONÓW > PRZYSŁUGUJĄCA LICZBA BONÓW
        if(entitledVouchersNumber.compareTo(new BigDecimal(order.getVouchersNumber())) < 0){
            violations.add(new EntityConstraintViolation("givenVouchersNumberMoreEntitledVouchersNumber", String.format("Przyznana liczba %s bonów jest większa niż przysługująca " +
                            "liczba %s bonów w programie dofinansowania '%s'", order.getVouchersNumber(), entitledVouchersNumber,
                            order.getApplication().getProgram().getProgramName())));
        }

        //PRZYZNANA LICZBA BONÓW = MIN(WNIOSKOWANA LICZBA BONÓW, PRZYSŁUGUJĄCA LICZBA BONÓW)
        BigDecimal fmin = new BigDecimal(order.getApplication().getBasicData().getVouchersNumber()).min(entitledVouchersNumber);
        if(!new BigDecimal(order.getVouchersNumber()).equals(fmin)){
            violations.add(new EntityConstraintViolation("givenVouchersNumberMin", String.format("Przyznana liczba %s bonów nie jest zgodna " +
                                                                        "z przysługującą wnioskowaną liczbą %s bonów",  order.getVouchersNumber(), fmin)));
        }


    }

}
