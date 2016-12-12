package pl.sodexo.it.gryf.service.local.impl.publicbenefits.importdata;

import com.google.common.base.Strings;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.*;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.CreateOrderDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderRepository;
import pl.sodexo.it.gryf.service.api.publicbenefits.orders.OrderService;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Isolution on 2016-12-02.
 */
@Service(value = "importOrderService")
public class ImportOrderServiceImpl extends ImportBaseDataServiceImpl {

    //PRIVATE FIELDS

    @Autowired
    private OrderService orderService;

    @Autowired
    private GryfValidator gryfValidator;

    @Autowired
    private OrderRepository orderRepository;

    //OVERRIDE

    @Override
    protected String saveData(ImportParamsDTO paramsDTO, Row row){
        ImportOrderDTO importDTO = createImportDTO(row);
        validateImport(paramsDTO, importDTO);

        CreateOrderDTO createOrderDTO = createCreateOrderDTO(importDTO);
        Long orderId = orderService.createOrder(createOrderDTO);
        return String.format("Poprawno utworzono dane: zamówienie (%s)", getIdToDescription(orderId));
    }

    //PRIVATE METHODS - VALIDATE

    private void validateImport(ImportParamsDTO paramsDTO, ImportOrderDTO importDTO){
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(importDTO);

        if(!Strings.isNullOrEmpty(importDTO.getExternalOrderId())){
            Integer orderNum = orderRepository.countByGrantProgramAndExternalOrderId(paramsDTO.getGramtProgramId(), importDTO.getExternalOrderId());
            if(orderNum > 0){
                violations.add(new EntityConstraintViolation(String.format("W systemie dla danego programu "
                        + "dofinansowania istnieje zamówienie o idnetyfikatorze [%s].", importDTO.getExternalOrderId())));
            }
        }
        gryfValidator.validate(violations);
    }

    //PRIVATE METHODS - CREATE IMPORT DTO

    private ImportOrderDTO createImportDTO(Row row){
        ImportOrderDTO o = new ImportOrderDTO();

        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();

            switch (cell.getColumnIndex()) {
                case 0:
                    o.setContractId(getLongCellValue(cell));
                    break;
                case 1:
                    o.setExternalOrderId(getStringCellValue(cell));
                    break;
                case 2:
                    o.setOrderDate(getDateCellValue(cell));
                    break;
                case 3:
                    o.setProductInstanceNum(getIntegerCellValue(cell));
                    break;
            }
        }
        return o;
    }

    //PRIVATE METHODS - CREATE BUSSINESS DTO

    private CreateOrderDTO createCreateOrderDTO(ImportOrderDTO importDTO){
        CreateOrderDTO dto = new CreateOrderDTO();
        dto.setContractId(importDTO.getContractId());
        dto.setExternalOrderId(importDTO.getExternalOrderId());
        dto.setOrderDate(importDTO.getOrderDate());
        dto.setProductInstanceNum(importDTO.getProductInstanceNum());
        return dto;
    }

}
