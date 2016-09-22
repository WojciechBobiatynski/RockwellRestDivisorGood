package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.actionTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderFlowElement;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.ActionBaseService;
import pl.sodexo.it.gryf.service.api.publicbenefits.grantapplications.GrantApplicationsService;
import pl.sodexo.it.gryf.service.api.publicbenefits.orders.OrderService;

import java.util.List;
import java.util.Set;

/**
 * Created by tomasz.bilski.ext on 2015-09-22.
 */
@Service
public class ToReject1ActionService extends ActionBaseService {

    //FIELDS

    @Autowired
    private OrderService orderService;

    @Autowired
    private GrantApplicationsService grantApplicationsService;

    //PUBLIC METHODS

    @Override
    protected void executeAction(Order order){

        //ADDRESSES
        Set<String> addresses = grantApplicationsService.getEmailRecipients(order.getApplication(), null);
        if(!GryfUtils.isEmpty(addresses)){
            String addressesStr = StringUtils.substring(GryfUtils.formatEmailRecipientsSet(addresses), 0, 500);
            orderService.addElementVarcharValue(order, "REJEMAIL1", addressesStr);
        }

        //REJECT REASON
        String reason = generateReason(order.getElements("CONFCHKBOX"));
        if(!StringUtils.isEmpty(reason)){
            orderService.addElementVarcharValue(order, "REJREASON1", reason);
        }
    }

    //PRIVATE METHODS

    private String generateReason(List<OrderElement> oeCheckboxes){
        StringBuilder sb = new StringBuilder();
        if(!GryfUtils.isEmpty(oeCheckboxes)){
            sb.append("Niepoprawne uzupełnienie pól na wniosku: ");
            OrderElement[] oeCheckboxTab =  oeCheckboxes.toArray(new OrderElement[oeCheckboxes.size()]);
            for (int i = 0; i < oeCheckboxTab.length; i++) {
                OrderFlowElement ofe = oeCheckboxTab[i].getOrderFlowElement();
                sb.append(ofe.getElementName());
                sb.append(i < oeCheckboxTab.length - 1 ? ", " : ".");
            }
        }
        return sb.toString();
    }
}
