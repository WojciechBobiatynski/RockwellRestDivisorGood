/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.root.service.local.publicbenefits.orders.elements.elementTypes.emailservices;

import pl.sodexo.it.gryf.dto.MailDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.elements.OrderElementDTOBuilder;

/**
 *
 * @author Marcel.GOLUNSKI
 */
public interface EmailDTOService {
    MailDTO createMailDTO(OrderElementDTOBuilder builder);
}
