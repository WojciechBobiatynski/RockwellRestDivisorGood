package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.actionTypes;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.mail.EmailSourceType;
import pl.sodexo.it.gryf.common.dto.mail.MailDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.MailAttachmentDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementComplexTypeEmailDTO;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.common.utils.JsonMapperUtils;
import pl.sodexo.it.gryf.model.publicbenefits.grantprograms.GrantProgramParam;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElement;
import pl.sodexo.it.gryf.model.publicbenefits.orders.OrderInvoice;
import pl.sodexo.it.gryf.service.local.api.MailService;
import pl.sodexo.it.gryf.service.local.api.ParamInDateService;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.actions.ActionBaseService;
import static pl.sodexo.it.gryf.model.publicbenefits.orders.OrderElementCons.*;

import java.util.Date;
import java.util.List;

/**
 * Created by Isolution on 2016-10-27.
 */
@Service
public class CareerDirectionSendOrderActionService extends ActionBaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CareerDirectionSendOrderActionService.class);

    @Autowired
    private MailService mailService;

    @Autowired
    private ApplicationParameters applicationParameters;

    @Autowired
    private ParamInDateService paramInDateService;

    public void execute(Order order, List<String> acceptedPathViolations){
        LOGGER.debug("Wysyłam zamówienie [{}]", order.getId());

        //ATTACHMENT
        MailAttachmentDTO attachmentDTO = createMailAttachmentDTO(order);

        //MAIL DATA
        OrderElement element = order.getElement(KK_ORDER_EMAIL_ELEM_ID);
        String mailStr = element.getClob().getValueClob();
        OrderElementComplexTypeEmailDTO elementDTO = JsonMapperUtils.readValue(mailStr, OrderElementComplexTypeEmailDTO.class);

        //ADRESY
        GrantProgramParam gppEmailFrom = paramInDateService.findGrantProgramParam(order.getContract().getGrantProgram().getId(), GrantProgramParam.EMAIL_FROM, new Date(), false);
        String emailFrom = gppEmailFrom != null ? gppEmailFrom.getValue() : applicationParameters.getGryfPbeDefPubEmailFrom();

        GrantProgramParam gppEmailReplayTo = paramInDateService.findGrantProgramParam(order.getContract().getGrantProgram().getId(), GrantProgramParam.EMAIL_REPLAY_TO, new Date(), false);
        String emailReplayTo = gppEmailReplayTo != null ? gppEmailReplayTo.getValue() : applicationParameters.getGryfPbeDefPubEmailReplyTo();

        MailDTO mailDTO = new MailDTO();
        mailDTO.setTemplateId(elementDTO.getEmailTemplateId());
        mailDTO.setAddressesFrom(emailFrom);
        mailDTO.setAddressesReplyTo(emailReplayTo);
        mailDTO.setSubject(elementDTO.getSubject());
        mailDTO.setBody(elementDTO.getBody());
        mailDTO.setAddressesTo(elementDTO.getAddressesTo());
        mailDTO.setSourceType(EmailSourceType.ORDER);
        mailDTO.setSourceId(order.getId());
        mailDTO.setAttachments(Lists.newArrayList(attachmentDTO));


        mailService.scheduleMail(mailDTO);
    }

    private MailAttachmentDTO createMailAttachmentDTO(Order order){
        OrderElement oeDocumentOwnContribution = order.getElement(KK_DOCUMENT_OWN_CONTRIBUTION_ELEM_ID);
        OrderElement oeAttachment01 = order.getElement(KK_ATTACHMENT_01_ELEM_ID);
        MailAttachmentDTO attachmentDTO = new MailAttachmentDTO();
        OrderInvoice orderInvoice = getOrderInvoice(order);

        if(!Strings.isNullOrEmpty(oeAttachment01.getValueVarchar())){
            attachmentDTO.setName(String.format("%s_nota_obciazeniowa.%s", GryfStringUtils.convertFileName(orderInvoice.getInvoiceNumber()),
                                                            GryfStringUtils.findFileExtension(oeAttachment01.getValueVarchar())));
            attachmentDTO.setPath(oeAttachment01.getValueVarchar());
        }else{
            attachmentDTO.setName(String.format("%s_nota_obciazeniowa.%s", GryfStringUtils.convertFileName(orderInvoice.getInvoiceNumber()),
                                                         GryfStringUtils.findFileExtension(oeDocumentOwnContribution.getValueVarchar())));
            attachmentDTO.setPath(oeDocumentOwnContribution.getValueVarchar());
        }
        return attachmentDTO;
    }

    private OrderInvoice getOrderInvoice(Order order){
        List<OrderInvoice> orderInvoices = order.getOrderInvoices();
        if(orderInvoices.size() == 0){
            throw new RuntimeException(String.format("Błąd parmetryzacji. Zamówienie [%s] nie zawiera żadnej noty.", order.getId()));
        }
        if(orderInvoices.size() > 1){
            throw new RuntimeException(String.format("Błąd parmetryzacji. Zamówienie [%s] zawiera wiecej niż jedną notę.", order.getId()));
        }
        return orderInvoices.get(0);
    }

}
