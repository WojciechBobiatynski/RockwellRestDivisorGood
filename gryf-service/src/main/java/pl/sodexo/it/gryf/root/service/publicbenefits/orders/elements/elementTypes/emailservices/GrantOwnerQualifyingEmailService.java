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
public class GrantOwnerQualifyingEmailService implements EmailDTOService {

    @Autowired
    private MailService mailService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private GrantApplicationsService grantApplicationsService;    
    
    @Override
    public MailDTO createMailDTO(OrderElementDTOBuilder builder) {
        Order order = builder.getOrder();
        
        String name, regNum, invoiceAddress, invoiceZipCode, invoiceCityName;
        if (order.getEnterprise() != null) {
            name = order.getEnterprise().getName();
            regNum = order.getEnterprise().getVatRegNum();
            invoiceAddress = order.getEnterprise().getAddressInvoice();
            invoiceZipCode = order.getEnterprise().getZipCodeInvoice().getZipCode();
            invoiceCityName = order.getEnterprise().getZipCodeInvoice().getCityName();
        } else {
            name = order.getIndividual().getFirstName() + " " + order.getIndividual().getLastName();
            regNum = order.getIndividual().getPesel();
            invoiceAddress = order.getIndividual().getAddressInvoice();
            invoiceZipCode = order.getIndividual().getZipCodeInvoice().getZipCode();
            invoiceCityName = order.getIndividual().getZipCodeInvoice().getCityName();
        }
        MailService.MailPlaceholders mailPlaceholders = mailService.createPlaceholders("grantProgramName", order.getApplication().getProgram().getProgramName())
                                                                   .add("grantedVouchersNumber",order.getVouchersNumber().toString())
                                                                   .add("enterpriseName", name)
                                                                   .add("enterpriseVatRegNum", regNum)
                                                                   .add("enterpriseAddress", invoiceAddress)
                                                                   .add("enterpriseZipCode", invoiceZipCode)
                                                                   .add("enterpriseCityName", invoiceCityName);
        return mailService.createMailDTO(EmailTemplate.GA_QLY_GO, 
                                         mailPlaceholders,
                                         order.getApplication().getProgram().getGrantOwner().getEmailAddressesGrantAppInfo());
    }
    
}
