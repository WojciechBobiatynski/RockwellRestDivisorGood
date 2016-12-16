package pl.sodexo.it.gryf.service.local.impl.publicbenefits.importdata;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.*;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.CreateOrderDTO;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.contracts.ContractRepository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.orders.OrderRepository;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.Training;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingCategory;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;
import pl.sodexo.it.gryf.service.api.publicbenefits.orders.OrderService;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.orders.OrderServiceLocal;

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

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    protected OrderServiceLocal orderServiceLocal;

    //OVERRIDE

    @Override
    protected ImportResultDTO saveInternalNormalData(ImportParamsDTO paramsDTO, Row row){
        ImportOrderDTO importDTO = createImportDTO(row);
        validateImport(paramsDTO, importDTO);

        Contract contract = contractRepository.get(importDTO.getContractId());
        validateConnectedData(importDTO, contract);

        CreateOrderDTO createOrderDTO = createCreateOrderDTO(importDTO, contract);
        Long orderId = orderService.createOrder(createOrderDTO);

        ImportResultDTO result = new ImportResultDTO();
        result.setOrderId(orderId);
        result.setDescrption(String.format("Poprawno utworzono dane: zamówienie (%s)", getIdToDescription(orderId)));
        return result;
    }

    //PRIVATE METHODS - VALIDATE

    private void validateImport(ImportParamsDTO paramsDTO, ImportOrderDTO importDTO){
        List<EntityConstraintViolation> violations = gryfValidator.generateViolation(importDTO);

        if(!Strings.isNullOrEmpty(importDTO.getExternalOrderId())){
            Integer orderNum = orderRepository.countByGrantProgramAndExternalOrderId(paramsDTO.getGrantProgramId(), importDTO.getExternalOrderId());
            if(orderNum > 0){
                violations.add(new EntityConstraintViolation(String.format("W systemie dla danego programu "
                        + "dofinansowania istnieje zamówienie o idnetyfikatorze [%s].", importDTO.getExternalOrderId())));
            }
        }
        gryfValidator.validate(violations);
    }

    private void validateConnectedData(ImportOrderDTO importDTO, Contract contract){
        List<EntityConstraintViolation> violations = Lists.newArrayList();

        if(contract == null){
            violations.add(new EntityConstraintViolation(String.format("Nie znaleziono umowy o identyfikatorze (%s).", importDTO.getContractId())));
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

    private CreateOrderDTO createCreateOrderDTO(ImportOrderDTO importDTO, Contract contract){
        CreateOrderDTO dto = orderServiceLocal.createCreateOrderDTO(contract);
        dto.setExternalOrderId(importDTO.getExternalOrderId());
        dto.setOrderDate(importDTO.getOrderDate());
        dto.setProductInstanceNum(importDTO.getProductInstanceNum());
        return dto;
    }

}
