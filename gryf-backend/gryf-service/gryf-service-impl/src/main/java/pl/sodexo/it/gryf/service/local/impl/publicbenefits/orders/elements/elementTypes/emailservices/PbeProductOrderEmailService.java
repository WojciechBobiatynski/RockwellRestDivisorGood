package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.elementTypes.emailservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.mail.MailDTO;
import pl.sodexo.it.gryf.common.mail.MailPlaceholders;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.model.mail.EmailTemplate;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElementDTOBuilder;
import pl.sodexo.it.gryf.service.local.api.MailService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.grantapplications.GrantApplicationEmailService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.OrderServiceLocal;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.elements.elementTypes.emailservices.EmailDTOService;

import java.text.NumberFormat;

/**
 * Created by Isolution on 2016-11-23.
 */
@Service
public class PbeProductOrderEmailService implements EmailDTOService {

    @Autowired
    private MailService mailService;

    @Autowired
    private OrderServiceLocal orderServiceLocal;

    @Autowired
    private GrantApplicationEmailService grantApplicationEmailService;

    @Override
    public MailDTO createMailDTO(OrderElementDTOBuilder builder) {
        Order order = builder.getOrder();
        MailPlaceholders mailPlaceholders = mailService.createPlaceholders("grantProgramName", "FFFF")
                .add("grantedVouchersNumber","FFFF")
                .add("paymentAmount", "FFFF")
                .add("accountNumber", "FFFF")
                .add("grantAppAddressCorr", "FFFF")
                .add("grantAppZipCorr", "FFFF")
                .add("grantAppCityCorr", "FFFF");
        return mailService.createMailDTO(EmailTemplate.GA_QUALIFY,
                mailPlaceholders,
                "dvsdv@scad");
    }

}
