/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.elementTypes.emailservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.MailDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.model.mail.EmailTemplate;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.service.local.api.MailService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.elementTypes.emailservices.EmailDTOService;
import pl.sodexo.it.gryf.service.api.publicbenefits.grantapplications.GrantApplicationsService;
import pl.sodexo.it.gryf.common.mail.MailPlaceholders;
import pl.sodexo.it.gryf.service.api.publicbenefits.orders.OrderService;

import java.text.NumberFormat;

/**
 *
 * @author Marcel.GOLUNSKI
 */
@Service
public class EnterpriseQualifyingEmailService implements EmailDTOService {

    @Autowired
    private MailService mailService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private GrantApplicationsService grantApplicationsService;    
    
    @Override
    public MailDTO createMailDTO(OrderElementDTOBuilder builder) {
        Order order = builder.getOrder();
        MailPlaceholders mailPlaceholders = mailService.createPlaceholders("grantProgramName", order.getApplication().getProgram().getProgramName())
                                                                   .add("grantedVouchersNumber",order.getVouchersNumber().toString())
                                                                   .add("paymentAmount", NumberFormat.getCurrencyInstance().format(orderService.getPaymentAmount(order)))
                                                                   .add("accountNumber", order.getEnterprise() != null ? order.getEnterprise().getAccountPayment() : order.getIndividual().getAccountPayment())
                                                                   .add("grantAppAddressCorr", order.getApplication().getBasicData().getAddressCorr())
                                                                   .add("grantAppZipCorr", order.getApplication().getBasicData().getZipCodeCorr().getZipCode())
                                                                   .add("grantAppCityCorr", order.getApplication().getBasicData().getZipCodeCorr().getCityName());
        return mailService.createMailDTO(EmailTemplate.GA_QUALIFY, 
                                         mailPlaceholders,
                                         GryfUtils.formatEmailRecipientsSet(grantApplicationsService.getEmailRecipients(order.getApplication(), null)));
    }
    
}
