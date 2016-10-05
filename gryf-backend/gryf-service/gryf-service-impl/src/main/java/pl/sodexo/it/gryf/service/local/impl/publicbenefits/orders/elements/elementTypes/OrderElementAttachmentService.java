package pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.elementTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements.OrderElementAttachmentDTO;
import pl.sodexo.it.gryf.common.enums.FileType;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.common.utils.StringUtils;
import pl.sodexo.it.gryf.model.publicbenefits.orders.*;
import pl.sodexo.it.gryf.service.api.other.ApplicationParameters;
import pl.sodexo.it.gryf.service.local.api.FileService;
import pl.sodexo.it.gryf.service.local.impl.publicbenefits.orders.elements.OrderElementBaseService;
import pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.orders.action.OrderElementDTOProvider;

import java.util.Date;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-08-21.
 */
@Service
public class OrderElementAttachmentService extends OrderElementBaseService<OrderElementAttachmentDTO> {

    //FIELDS

    @Autowired
    private FileService fileService;

    @Autowired
    protected ApplicationParameters applicationParameters;

    //PUBLIC METHODS

    @Override
    public OrderElementAttachmentDTO createElement(OrderElementDTOBuilder builder) {
        return OrderElementDTOProvider.createOrderElementAttachmentDTO(builder);
    }

    @Override
    public void updateValue(OrderElement element, OrderElementAttachmentDTO dto) {
        if(dto.isFileToDelete()){
            String fileLocation = element.getValueVarchar();
            if(!StringUtils.isEmpty(fileLocation)) {
                fileService.deleteFile(fileLocation);
                element.setValueVarchar(null);
                element.setCompletedDate(null);
                //TODO: co z usunietym plikiem jak wyjatek??
            }
        }
        if(dto.getFile() != null){
            String fileName = findAttachmentName(element);
            String fileLocation = fileService.writeFile(FileType.ORDERS, fileName, dto.getFile(), element);
            element.setValueVarchar(fileLocation);
            element.setCompletedDate(new Date());
        }

    }

    public void updateValue(OrderElement element, String fileLocation) {
        element.setValueVarchar(fileLocation);
        element.setCompletedDate(!StringUtils.isEmpty(fileLocation) ? new Date() : null);
    }

    //PRIVATE METHODS

    private String findAttachmentName(OrderElement element){
        Order order = element.getOrder();
        OrderFlowElement orderFlowElement = element.getOrderFlowElement();
        
        Long entityId = order.getEnterprise() != null ? order.getEnterprise().getId() : order.getIndividual().getId();

        String fileName = String.format("%s_%s_%s_%s", entityId, order.getId(),
                                            element.getId(), orderFlowElement.getElementName());
        return StringUtils.convertFileName(fileName);

    }

    //PROTECTED METHODS - OVERRIDE VALIDATION

    @Override
    protected boolean isValueInserted(OrderElement orderElement, OrderElementAttachmentDTO dto){
        String valOld = orderElement.getValueVarchar();
        FileDTO valNew = dto.getFile();
        return StringUtils.isEmpty(valOld) && valNew != null;
    }

    @Override
    protected boolean isValueUpdated(OrderElement orderElement, OrderElementAttachmentDTO dto){
        String valOld = orderElement.getValueVarchar();
        FileDTO valNew = dto.getFile();
        return (StringUtils.isEmpty(valOld) && valNew != null) ||
                (!StringUtils.isEmpty(valOld) && dto.isFileToDelete());
    }

    @Override
    protected boolean isValueFilled(OrderElement orderElement, OrderElementAttachmentDTO dto){
        String valOld = orderElement.getValueVarchar();
        FileDTO valNew = dto.getFile();
        if(valNew == null){
            return !StringUtils.isEmpty(valOld);
        }
        return true;
    }

    @Override
    protected void addViolation(List<EntityConstraintViolation> violations, OrderElementAttachmentDTO dto, String message){
        violations.add(new EntityConstraintViolation(dto.getOrderFlowElementId(), message, dto.getFile() != null ? dto.getFile().getOriginalFilename() : null));
    }

    @Override
    protected void validateCustom(List<EntityConstraintViolation> violations, OrderElement orderElement, OrderFlowElementInStatus orderFlowElementInStatus, OrderElementAttachmentDTO dto) {
        OrderFlowElement orderFlowElement = orderFlowElementInStatus.getOrderFlowElement();

        //FILE MAX SIZE
        int attachmentMaxSize = applicationParameters.getAttachmentMaxSize();
        int attachmentMaxSizeMB = applicationParameters.getAttachmentMaxSizeMB();
        if (dto.getFile() != null) {
            if (dto.getFile().getSize() > attachmentMaxSize) {
                addViolation(violations, dto, String.format("Plik dla załącznika '%s' jest zbyt duży - maksymalna wielkość załącznika to %sMB",
                        orderFlowElement.getElementName(), attachmentMaxSizeMB));
            }
        }
    }

}
