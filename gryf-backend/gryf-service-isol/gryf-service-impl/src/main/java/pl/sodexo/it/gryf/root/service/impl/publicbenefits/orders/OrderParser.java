package pl.sodexo.it.gryf.root.service.impl.publicbenefits.orders;

import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.action.IncomingOrderElementDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementDTO;
import pl.sodexo.it.gryf.common.utils.JsonMapperUtils;
import pl.sodexo.it.gryf.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-08-19.
 */
public class OrderParser {

    //ORDERS

    public static List<IncomingOrderElementDTO> readIncomingOrderElements(String data) {
        IncomingOrderElementDTO[] values = JsonMapperUtils.readValue(data, IncomingOrderElementDTO[].class);
        return Arrays.asList(values);
    }

    public static OrderElementDTO readOrderElement(String dtoClassName, String data) {

        //RETRIVE CLASS
        Class<?> clazz;
        try {
            clazz = Class.forName(dtoClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Nie znaleziono klasy dla dto elementu - nazwa klasy " + dtoClassName);
        }

        return(OrderElementDTO) JsonMapperUtils.readValue(data, clazz);
    }

    //ACCEPTED VIOLATIONS

    public static List<String> readAcceptedViolations(String acceptedViolationsStr) {
        if (StringUtils.isEmpty(acceptedViolationsStr)) {
            return new ArrayList<>();
        }
        String[] acceptedViolationsTab = JsonMapperUtils.readValue(acceptedViolationsStr, String[].class);
        return new ArrayList<>(Arrays.asList(acceptedViolationsTab));
    }

}
