package pl.sodexo.it.gryf.service.mapping.entitytodto.publicbenefits.orders.action;

import com.google.common.collect.Lists;
import pl.sodexo.it.gryf.common.dto.mail.MailDTO;
import pl.sodexo.it.gryf.common.dto.other.DictionaryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.invoices.PaymentDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.*;
import pl.sodexo.it.gryf.common.dto.zipcodes.detailsform.ZipCodeDto;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.common.utils.GryfUtils;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationBasicData;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationContactData;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationContactType;
import pl.sodexo.it.gryf.model.publicbenefits.invoices.Payment;
import pl.sodexo.it.gryf.model.publicbenefits.orders.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akmiecinski on 2016-09-30.
 */
public final class OrderElementDTOProvider {

    private OrderElementDTOProvider() {
    }

    private static OrderElementDTO createOrderElementDTO(OrderElementDTOBuilder builder, OrderElementDTO dto) {
        dto.setId(builder.getElement().getId());
        dto.setOrderFlowElementId(builder.getOrderFlowElement().getElementId());
        dto.setName(builder.getOrderFlowElement().getElementName());
        dto.setElementTypeComponentName(builder.getOrderFlowElement().getOrderFlowElementType().getComponentName());
        dto.setElementTypeParams(builder.getOrderFlowElement().getElementTypeParams());
        dto.setFlags(calculateFlags(builder));
        dto.setPrivilege((builder.getOrderFlowElementInStatus() != null) ? builder.getOrderFlowElementInStatus().getAugIdRequired() : null);
        dto.setRequiredDate(builder.getElement().getRequiredDate());
        dto.setCompletedDate(builder.getElement().getCompletedDate());
        return dto;
    }

    public static OrderElementAttachmentDTO createOrderElementAttachmentDTO(OrderElementDTOBuilder builder) {
        OrderElementAttachmentDTO dto = (OrderElementAttachmentDTO) createOrderElementDTO(builder, new OrderElementAttachmentDTO());
        if(!GryfStringUtils.isEmpty(builder.getElement().getValueVarchar())) {
            dto.setFileName(GryfStringUtils.findFileNameInPath(builder.getElement().getValueVarchar()));
        }
        return dto;
    }

    public static OrderElementAttrDDTO createOrderElementAttrDDTO(OrderElementDTOBuilder builder) {
        OrderElementAttrDDTO dto = (OrderElementAttrDDTO) createOrderElementDTO(builder, new OrderElementAttrDDTO());
        dto.setValueDate(builder.getElement().getValueDate());
        return dto;
    }

    public static OrderElementAttrNDTO createOrderElementAttrNDTO(OrderElementDTOBuilder builder) {
        OrderElementAttrNDTO dto = (OrderElementAttrNDTO) createOrderElementDTO(builder, new OrderElementAttrNDTO());
        dto.setValueNumber(builder.getElement().getValueNumber());
        return dto;
    }

    public static OrderElementAttrVDTO createOrderElementAttrVDTO(OrderElementDTOBuilder builder) {
        OrderElementAttrVDTO dto = (OrderElementAttrVDTO) createOrderElementDTO(builder, new OrderElementAttrVDTO());
        dto.setValueVarchar(builder.getElement().getValueVarchar());
        return dto;
    }

    public static OrderElementCheckboxDTO createOrderElementCheckboxDTO(OrderElementDTOBuilder builder) {
        OrderElementCheckboxDTO dto = (OrderElementCheckboxDTO) createOrderElementDTO(builder, new OrderElementCheckboxDTO());
        dto.setIsSelected(Boolean.valueOf(builder.getElement().getValueVarchar()));
        return dto;
    }

    public static OrderElementComboboxDTO createOrderElementComboboxDTO(final OrderElementDTOBuilder builder, List<DictionaryDTO> items) {
        OrderElementComboboxDTO dto = (OrderElementComboboxDTO) createOrderElementDTO(builder, new OrderElementComboboxDTO());
        dto.setItems(items);
        dto.setSelectedItem(GryfUtils.find(items, input -> input.getId().equals(builder.getElement().getValueVarchar())));
        return dto;
    }

    public static OrderElementComplexTypeBasicGrantAppInfoDTO createOrderElementComplexTypeBasicGrantAppInfoDTO(OrderElementDTOBuilder builder) {
        OrderElementComplexTypeBasicGrantAppInfoDTO dto = (OrderElementComplexTypeBasicGrantAppInfoDTO) createOrderElementDTO(builder, new OrderElementComplexTypeBasicGrantAppInfoDTO());
        GrantApplication application = builder.getOrder().getApplication();
        dto.setApplicationId(application.getId());
        dto.setReceiptDate(application.getReceiptDate());
        dto.setApplyDate(application.getApplyDate());
        dto.setConsiderationDate(application.getConsiderationDate());
        dto.setOperator(application.getCreatedUser());
        return dto;
    }

    public static OrderElementComplexTypeBasicOrderInfoDTO createOrderElementComplexTypeBasicOrderInfoDTO(OrderElementDTOBuilder builder) {
        OrderElementComplexTypeBasicOrderInfoDTO dto = (OrderElementComplexTypeBasicOrderInfoDTO) createOrderElementDTO(builder, new OrderElementComplexTypeBasicOrderInfoDTO());
        Order order = builder.getOrder();
        dto.setOrderId(order.getId());
        dto.setStatusId(order.getStatus().getStatusId());
        dto.setStatusName(order.getStatus().getStatusName());
        dto.setOrderDate(order.getOrderDate());
        dto.setExternalOrderId(order.getExternalOrderId());
        dto.setOperator(order.getOperator());
        dto.setEnterpriseId((order.getEnterprise() != null) ? order.getEnterprise().getId() : null);
        dto.setEnterpriseName((order.getEnterprise() != null) ? order.getEnterprise().getName() : null);
        dto.setVatRegNum((order.getEnterprise() != null) ? order.getEnterprise().getVatRegNum() : null);
        dto.setIndividualId((order.getContract() != null && order.getContract().getIndividual() != null) ? order.getContract().getIndividual().getId() : null);
        dto.setIndividualName((order.getContract() != null && order.getContract().getIndividual() != null) ? order.getContract().getIndividual().getFirstName() + " " + order.getContract().getIndividual().getLastName() : null);
        dto.setPesel((order.getContract() != null && order.getContract().getIndividual() != null) ? order.getContract().getIndividual().getPesel() : null);
        return dto;
    }

    public static OrderElementComplexTypePbeProductInfoDTO createOrderElementComplexTypePbeProductInfoDTO(OrderElementDTOBuilder builder,
            Integer productInstanceNum, BigDecimal productInstanceAmount, BigDecimal ownContributionPercent,
            BigDecimal ownContributionAmont, BigDecimal grantAmount, BigDecimal orderAmount) {
        OrderElementComplexTypePbeProductInfoDTO dto = (OrderElementComplexTypePbeProductInfoDTO) createOrderElementDTO(builder, new OrderElementComplexTypePbeProductInfoDTO());
        dto.setProductInstanceNum(productInstanceNum);
        dto.setProductInstanceAmount(productInstanceAmount);
        dto.setOwnContributionPercen(ownContributionPercent);
        dto.setOwnContributionAmont(ownContributionAmont);
        dto.setGrantAmount(grantAmount);
        dto.setOrderAmount(orderAmount);
        return dto;
    }

    public static OrderElementComplexTypeBasicContractInfoDTO createOrderElementComplexTypeBasicContractInfoDTO(OrderElementDTOBuilder builder) {
        OrderElementComplexTypeBasicContractInfoDTO dto = (OrderElementComplexTypeBasicContractInfoDTO) createOrderElementDTO(builder, new OrderElementComplexTypeBasicContractInfoDTO());
        Order order = builder.getOrder();
        if(order.getContract() != null){
            Contract contract = order.getContract();
            dto.setContractId(contract.getId());
            dto.setContractTypeName(contract.getContractType() != null ? contract.getContractType().getName() : null);
            dto.setContractSignDate(contract.getSignDate());
            dto.setContractExpiryDate(contract.getExpiryDate());
            if(contract.getGrantProgram() != null){
                dto.setGrantProgramName(contract.getGrantProgram().getProgramName());
            }
            if(contract.getIndividual() != null){
                dto.setIndividualFirstName(contract.getIndividual().getFirstName());
                dto.setIndividualLastName(contract.getIndividual().getLastName());
                dto.setIndividualPesel(contract.getIndividual().getPesel());
                dto.setAccountPayment(contract.getIndividual().getAccountPayment());
            }
            if(contract.getEnterprise() != null){
                dto.setEnterpriseName(contract.getEnterprise().getName());
                dto.setEnterpriseVatRegNum(contract.getEnterprise().getVatRegNum());
                dto.setAccountPayment(contract.getEnterprise().getAccountPayment());
            }
        }

        return dto;
    }

    public static OrderElementComplexTypePaymentListInfoDTO createOrderElementComplexTypePaymentListInfoDTO(OrderElementDTOBuilder builder, List<Payment> payments) {
        OrderElementComplexTypePaymentListInfoDTO dto = (OrderElementComplexTypePaymentListInfoDTO) createOrderElementDTO(builder, new OrderElementComplexTypePaymentListInfoDTO());
        dto.setPayments(Lists.newArrayListWithCapacity(payments.size()));
        for (Payment payment : payments){
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setId(payment.getId());
            paymentDTO.setUsed(payment.getUsed());
            paymentDTO.setAmount(payment.getAmount());
            paymentDTO.setTransferAmount(payment.getTransferAmount());
            paymentDTO.setMatchDate(payment.getMatchDate());
            paymentDTO.setPaymentDate(payment.getPaymentDate());
            paymentDTO.setTransferDate(payment.getTransferDate());
            paymentDTO.setTransferDetail(payment.getTransferDetail());
            dto.getPayments().add(paymentDTO);
        }
        return dto;
    }

    public static OrderElementComplexTypeAddressInfoDTO createOrderElementComplexTypeAddressInfoDTO(OrderElementDTOBuilder builder,
                                                                                    String addressInvoice, ZipCodeDto zipCodeInvoiceDTO,
                                                                                    String addressCorr, ZipCodeDto zipCodeCorrDTO){
        OrderElementComplexTypeAddressInfoDTO dto = (OrderElementComplexTypeAddressInfoDTO) createOrderElementDTO(builder, new OrderElementComplexTypeAddressInfoDTO());
        dto.setAddressInvoice(addressInvoice);
        dto.setZipCodeInvoice(zipCodeInvoiceDTO);
        dto.setAddressCorr(addressCorr);
        dto.setZipCodeCorr(zipCodeCorrDTO);
        return dto;
    }


    public static OrderElementComplexTypeEmailDTO createOrderElementComplexTypeEmailDTO(OrderElementDTOBuilder builder, MailDTO mailDTO){
        OrderElementComplexTypeEmailDTO dto = (OrderElementComplexTypeEmailDTO) createOrderElementDTO(builder, new OrderElementComplexTypeEmailDTO());
        dto.setAddressesTo(mailDTO.getAddressesTo());
        dto.setSubject(mailDTO.getSubject());
        dto.setBody(mailDTO.getBody());
        dto.setEmailTemplateId(mailDTO.getTemplateId());
        dto.setEmailServiceBean(builder.getOrderFlowElement().getPropertyValue(OrderFlowElement.EMAIL_SERVICE_BEAN));
        return dto;
    }

    public static OrderElementComplexTypeEmailDTO createOrderElementComplexTypeEmailDTO(OrderElementDTOBuilder builder, OrderElementComplexTypeEmailDTO mailDto){
        OrderElementComplexTypeEmailDTO dto = (OrderElementComplexTypeEmailDTO) createOrderElementDTO(builder, new OrderElementComplexTypeEmailDTO());
        dto.setAddressesTo(mailDto.getAddressesTo());
        dto.setSubject(mailDto.getSubject());
        dto.setBody(mailDto.getBody());
        dto.setEmailTemplateId(mailDto.getEmailTemplateId());
        dto.setEmailServiceBean(builder.getOrderFlowElement().getPropertyValue(OrderFlowElement.EMAIL_SERVICE_BEAN));
        return dto;
    }

    public static OrderElementComplexTypeEnterprisePaymentInfoDTO createOrderElementComplexTypeEnterprisePaymentInfoDTO(OrderElementDTOBuilder builder, BigDecimal paymentAmount) {
        OrderElementComplexTypeEnterprisePaymentInfoDTO dto = (OrderElementComplexTypeEnterprisePaymentInfoDTO) createOrderElementDTO(builder, new OrderElementComplexTypeEnterprisePaymentInfoDTO());
        Order order = builder.getOrder();
        dto.setGivenVouchersNumber(order.getVouchersNumber());
        dto.setPaymentAmount(paymentAmount);
        dto.setPaymentAccount(order.getEnterprise() != null ? order.getEnterprise().getAccountPayment() :
                        (order.getContract() != null && order.getContract().getIndividual() != null ?
                                order.getContract().getIndividual().getAccountPayment() : null));
        return dto;
    }

    public static OrderElementComplexTypeGrantApplicationContactDataDTO createOrderElementComplexTypeGrantApplicationContactDataDTO(OrderElementDTOBuilder builder) {
        OrderElementComplexTypeGrantApplicationContactDataDTO dto = (OrderElementComplexTypeGrantApplicationContactDataDTO) createOrderElementDTO(builder, new OrderElementComplexTypeGrantApplicationContactDataDTO());
        dto.setContacts(new ArrayList<>());

        Order order = builder.getOrder();
        GrantApplication application = order.getApplication();
        GrantApplicationBasicData basicData = application.getBasicData();
        for (GrantApplicationContactData contact : basicData.getContacts()) {

            if(contact.getContactType() == GrantApplicationContactType.CONTACT) {
                OrderElementComplexTypeGrantApplicationContactDataDTO.ContactDataDTO contactDTO = new OrderElementComplexTypeGrantApplicationContactDataDTO.ContactDataDTO();
                contactDTO.setName(contact.getName());
                contactDTO.setEmail(contact.getEmail());
                contactDTO.setPhone(contact.getPhone());
                dto.getContacts().add(contactDTO);
            }
        }
        return dto;
    }

    public static OrderElementComplexTypeGrantApplicationDTO createOrderElementComplexTypeGrantApplicationDTO(OrderElementDTOBuilder builder) {
        OrderElementComplexTypeGrantApplicationDTO dto = (OrderElementComplexTypeGrantApplicationDTO) createOrderElementDTO(builder, new OrderElementComplexTypeGrantApplicationDTO());
        dto.setGrantApplicationId(builder.getOrder().getApplication().getId());
        return dto;
    }

    public static OrderElementComplexTypeGrantedVouchersInfoDTO createOrderElementComplexTypeGrantedVouchersInfoDTO(OrderElementDTOBuilder builder,
            Integer entitledVouchersNumber,
            BigDecimal entitledPlnAmount, BigDecimal limitEurAmount,
            BigDecimal exchange) {
        OrderElementComplexTypeGrantedVouchersInfoDTO dto = (OrderElementComplexTypeGrantedVouchersInfoDTO) createOrderElementDTO(builder, new OrderElementComplexTypeGrantedVouchersInfoDTO());
        Order order = builder.getOrder();

        dto.setEntitledVouchersNumber(entitledVouchersNumber);
        dto.setEntitledPlnAmount(entitledPlnAmount);
        dto.setRequestedVouchersNumber(order.getApplication().getBasicData().getVouchersNumber());
        dto.setLimitEurAmount(limitEurAmount);
        dto.setExchange(exchange);
        if (order.getVouchersNumber() != null){
            dto.setGivenVouchersNumber(order.getVouchersNumber());
        } else{
            dto.setGivenVouchersNumber(Math.min(entitledVouchersNumber, order.getApplication().getBasicData().getVouchersNumber()));
        }
        dto.setVoucherAidValue((order.getProduct() != null) ? order.getProduct().getPbeAidValue() : null);
        return dto;
    }

    public static OrderElementConfirmationCheckboxDTO createOrderElementConfirmationCheckboxDTO(OrderElementDTOBuilder builder) {
        OrderElementConfirmationCheckboxDTO dto = (OrderElementConfirmationCheckboxDTO) createOrderElementDTO(builder, new OrderElementConfirmationCheckboxDTO());
        dto.setIsSelected(Boolean.valueOf(builder.getElement().getValueVarchar()));
        return dto;
    }

    public static OrderElementHeadingDTO createOrderElementHeadingDTO(OrderElementDTOBuilder builder) {
        return (OrderElementHeadingDTO) createOrderElementDTO(builder, new OrderElementHeadingDTO());
    }

    //PRIVATE METHODS

    private static String calculateFlags(OrderElementDTOBuilder builder){
        StringBuilder sb = new StringBuilder();

        OrderFlowElementInStatus ofeis = builder.getOrderFlowElementInStatus();
        if(ofeis != null && !GryfStringUtils.isEmpty(ofeis.getFlags())){
            sb.append(ofeis.getFlags().trim());
        }

        List<OrderFlowStatusTransitionElementFlag> flags = builder.getOrderFlowStatusTransitionElementFlags();
        if(flags != null){
            for(OrderFlowStatusTransitionElementFlag flag : flags){
                if(flag != null && !GryfStringUtils.isEmpty(flag.getFlags())){
                    sb.append(flag.getFlags().replace(OrderFlowStatusTransitionElementFlag.FLAG_MANDATORY, "").trim());
                }
            }
        }

        return GryfStringUtils.removeDuplicateCharacters(sb.toString());
    }

}
