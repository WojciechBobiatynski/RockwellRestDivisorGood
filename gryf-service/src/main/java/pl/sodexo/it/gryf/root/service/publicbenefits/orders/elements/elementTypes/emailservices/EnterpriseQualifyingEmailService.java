/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.root.service.publicbenefits.orders.elements.elementTypes.emailservices;

import java.text.NumberFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.dto.MailDTO;
import pl.sodexo.it.gryf.dto.publicbenefits.orders.detailsform.elements.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.model.mail.EmailTemplate;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.root.service.MailService;
import pl.sodexo.it.gryf.root.service.publicbenefits.grantapplications.GrantApplicationsService;
import pl.sodexo.it.gryf.root.service.publicbenefits.orders.OrderService;
import pl.sodexo.it.gryf.utils.GryfUtils;

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
        MailService.MailPlaceholders mailPlaceholders = mailService.createPlaceholders("grantProgramName", order.getApplication().getProgram().getProgramName())
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
