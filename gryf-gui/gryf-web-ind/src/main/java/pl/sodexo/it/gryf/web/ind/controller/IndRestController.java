package pl.sodexo.it.gryf.web.ind.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.sodexo.it.gryf.web.ind.dto.IndDto;
import pl.sodexo.it.gryf.web.ind.dto.ProductDto;
import pl.sodexo.it.gryf.web.ind.dto.TrainingDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by adziobek on 20.10.2016.
 *
 * Kontroler do obługi panelu uczestnika.
 */
@RestController
@RequestMapping(value = "/rest/ind", produces = "application/json;charset=UTF-8")
public class IndRestController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public IndDto getTrainingInstitutionById() {
        IndDto indDto = getDefaultInd();
        return indDto;
    }

    private IndDto getDefaultInd() {
        IndDto indDto = new IndDto();

        indDto.setFirstName("Jacek");
        indDto.setLastName("Kowalski");
        indDto.setPesel("93041500311");
        indDto.setAgreementId("23");
        indDto.setAgreementSigningDate(new Date());
        indDto.setTrainingCategory("INFORMATYCZNE");

        List<ProductDto> productDtoList = new ArrayList<>();
        ProductDto productDto1 = new ProductDto();
        productDto1.setExpirationDate(new Date());
        productDto1.setAvailableProductsCount(123);
        productDto1.setGrantedProductsCount(180);
        productDto1.setOrderDate(new Date());
        productDto1.setOrderId("124");
        productDto1.setReservedProductsCount(90);
        productDto1.setUsedProductsCount(45);
        productDtoList.add(productDto1);

        List<TrainingDto> reservedTraining = new ArrayList<>();
        TrainingDto trainingDto = new TrainingDto();
        trainingDto.setStartDate(new Date());
        trainingDto.setEndDate(new Date());
        trainingDto.setName("Szkolenie z programowania w języku Java");
        trainingDto.setTrainingInstitutionName("ALTKOM");
        trainingDto.setReservedProductsCount(60);
        trainingDto.setOrderProductsDate(new Date());
        reservedTraining.add(trainingDto);


        List<TrainingDto> settledTraining = new ArrayList<>();
        TrainingDto trainingDto2 = new TrainingDto();
        trainingDto2.setStartDate(new Date());
        trainingDto2.setEndDate(new Date());
        trainingDto2.setName("Szkolenie z programowania w języku Python");
        trainingDto2.setTrainingInstitutionName("SAMSUNG");
        trainingDto2.setSettledProductsCount(22);
        settledTraining.add(trainingDto2);

        indDto.setProducts(productDtoList);
        indDto.setReservedTraining(reservedTraining);
        indDto.setSettledTraining(settledTraining);

        return indDto;
    }
}