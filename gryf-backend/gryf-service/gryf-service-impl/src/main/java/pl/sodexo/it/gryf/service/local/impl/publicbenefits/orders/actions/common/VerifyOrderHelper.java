package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.common;

import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowElementType.ElementType;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.elementTypes.OrderElementComplexTypeGrantedVouchersInfoServiceImpl;

import java.math.BigDecimal;
import java.util.List;

import static pl.sodexo.it.gryf.common.utils.GryfConstants.BIG_DECIMAL_INTEGER_SCALE;

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
        OrderElement oeLimitEurAmount         = order.getElement(OrderElementComplexTypeGrantedVouchersInfoServiceImpl.LIMIT_EUR_AMOUNT_ELEM_ID);
        OrderElement oeEntitledPlnAmount      = order.getElement(OrderElementComplexTypeGrantedVouchersInfoServiceImpl.ENTITLED_PLN_AMOUNT_ELEM_ID);
        OrderElement oeExchange               = order.getElement(OrderElementComplexTypeGrantedVouchersInfoServiceImpl.EXCHANGE_ELEM_ID);
        OrderElement oeEntitledVouchersNumber = order.getElement(OrderElementComplexTypeGrantedVouchersInfoServiceImpl.ENTITLED_VOUCHERS_NUMBER_ELEM_ID);

        BigDecimal limitEurAmount         = (oeLimitEurAmount != null && oeLimitEurAmount.getValueNumber() !=null)                 ? oeLimitEurAmount.getValueNumber()         : BigDecimal.ZERO;
        BigDecimal entitledPlnAmount      = (oeEntitledPlnAmount != null && oeEntitledPlnAmount.getValueNumber() !=null)           ? oeEntitledPlnAmount.getValueNumber()      : BigDecimal.ZERO;
        BigDecimal exchange               = (oeExchange != null && oeExchange.getValueNumber() !=null)                             ? oeExchange.getValueNumber()               : BigDecimal.ZERO;
        BigDecimal entitledVouchersNumber = (oeEntitledVouchersNumber != null && oeEntitledVouchersNumber.getValueNumber() !=null) ? oeEntitledVouchersNumber.getValueNumber() : BigDecimal.ZERO;
        BigDecimal voucherAidValue        = (order.getProduct() != null && order.getProduct().getPbeAidValue() != null)            ? order.getProduct().getPbeAidValue()       : BigDecimal.ZERO;

        BigDecimal given;
        if (limitEurAmount.multiply(exchange).compareTo(entitledPlnAmount) >= 0) {
            given = entitledVouchersNumber;
        } else {
            given = limitEurAmount.multiply(exchange).divide(voucherAidValue, BIG_DECIMAL_INTEGER_SCALE, BigDecimal.ROUND_FLOOR);
        }

        //PRZYZNANA LICZBA BONÓW - DOBRZE OBLICOZNA
        if(!given.equals(BigDecimal.valueOf(order.getVouchersNumber()))){
            violations.add(new EntityConstraintViolation("givenVouchersNumberBadCalculation", String.format("Przyznana liczba %s bonów nie zgadza się z limitem pomocy %s i kursem EUR %s",
                                                                            order.getVouchersNumber(), limitEurAmount, exchange)));
        }

        //PRZYZNANA LICZBA BONÓW > PRZYSŁUGUJĄCA LICZBA BONÓW
        if(entitledVouchersNumber.compareTo(BigDecimal.valueOf(order.getVouchersNumber())) < 0){
            violations.add(new EntityConstraintViolation("givenVouchersNumberMoreEntitledVouchersNumber", String.format("Przyznana liczba %s bonów jest większa niż przysługująca " +
                            "liczba %s bonów w programie dofinansowania '%s'", order.getVouchersNumber(), entitledVouchersNumber,
                            order.getApplication().getProgram().getProgramName())));
        }

        //PRZYZNANA LICZBA BONÓW = MIN(WNIOSKOWANA LICZBA BONÓW, PRZYSŁUGUJĄCA LICZBA BONÓW)
        BigDecimal fmin = BigDecimal.valueOf(order.getApplication().getBasicData().getVouchersNumber()).min(entitledVouchersNumber);
        if(!BigDecimal.valueOf(order.getVouchersNumber()).equals(fmin)){
            violations.add(new EntityConstraintViolation("givenVouchersNumberMin", String.format("Przyznana liczba %s bonów nie jest zgodna " +
                                                                        "z przysługującą wnioskowaną liczbą %s bonów",  order.getVouchersNumber(), fmin)));
        }


    }

}
