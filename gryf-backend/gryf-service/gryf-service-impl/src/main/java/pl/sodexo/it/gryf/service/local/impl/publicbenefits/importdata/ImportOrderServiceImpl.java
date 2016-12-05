package pl.sodexo.it.gryf.service.local.impl.publicbenefits.importdata;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.*;
import pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.CreateOrderDTO;
import pl.sodexo.it.gryf.service.api.publicbenefits.orders.OrderService;

import java.util.Iterator;

/**
 * Created by Isolution on 2016-12-02.
 */
@Service(value = "importOrderDataService")
public class ImportOrderServiceImpl extends ImportBaseDataServiceImpl {

    //PRIVATE FIELDS

    @Autowired
    private OrderService orderService;

    //OVERRIDE

    @Override
    protected String saveData(Row row){
        ImportOrderDTO importDTO = createImportDTO(row);
        CreateOrderDTO createOrderDTO = createCreateOrderDTO(importDTO);
        Long orderId = orderService.createOrder(createOrderDTO);

        return String.format("Poprawno zapisano dane: zamówienie (%s)", orderId);
    }

    //PRIVATE METHODS - CREATE IMPORT DTO

    private ImportOrderDTO createImportDTO(Row row){
        ImportOrderDTO o = new ImportOrderDTO();

        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();

            switch (cell.getColumnIndex()) {
                case 0:
                    o.setContractId((long)cell.getNumericCellValue());
                    break;
                case 1:
                    o.setExternalOrderId(cell.getStringCellValue());
                    break;
                case 2:
                    o.setOrderDate(cell.getDateCellValue());
                    break;
                case 3:
                    o.setProductInstanceNum((int)cell.getNumericCellValue());
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
