package pl.sodexo.it.gryf.web.ind.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.sodexo.it.gryf.web.ind.dto.AgreementDto;
import pl.sodexo.it.gryf.web.ind.dto.IndDto;
import pl.sodexo.it.gryf.web.ind.dto.ProductDto;
import pl.sodexo.it.gryf.web.ind.dto.TrainingDto;
import pl.sodexo.it.gryf.web.ind.dto.TrainingStatus;

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
public class IndividualsRestController {

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
        indDto.setEmail("john.smith@company.com");

        AgreementDto agreementDto1 = new AgreementDto();
        agreementDto1.setId("ID612/12122016");
        agreementDto1.setSigningDate(new Date());
        agreementDto1.setTrainingCategory("Szkolenie XYZ");

        AgreementDto agreementDto2 = new AgreementDto();
        agreementDto2.setId("ID123/31122016");
        agreementDto2.setSigningDate(new Date());
        agreementDto2.setTrainingCategory("Szkolenie ABC");

        List<AgreementDto> agreementList = new ArrayList<>();
        agreementList.add(agreementDto1);
        agreementList.add(agreementDto2);

        List<ProductDto> productDtoList = new ArrayList<>();
        productDtoList.add(new ProductDto(new Date(), 36, 12, 12, 12, new Date(), "AZ/1234/12122016"));
        productDtoList.add(new ProductDto(new Date(), 112, 12, 12, 12, new Date(), "AZ/1235/12122016"));
        productDtoList.add(new ProductDto(new Date(), 122, 12, 12, 12, new Date(), "AZ/3435/210112017"));

        List<TrainingDto> trainingList = new ArrayList<>();
        trainingList.add(new TrainingDto("Szkolenie z programowania w języku Java", "ALTKOM", 60, new Date(), new Date(), new Date(), TrainingStatus.SETTLED.getName()));
        trainingList.add(new TrainingDto("Inne szkolenie", "Instytucja 1", 40, new Date(), new Date(), new Date(), TrainingStatus.RESERVED.getName()));
        trainingList.add(new TrainingDto("Python Master 3", "SAMSUNG", 22, new Date(), new Date(), new Date(), TrainingStatus.SETTLED.getName()));
        trainingList.add(new TrainingDto("Kurs na egzaminatora WORD", "WORD Kraków", 54, new Date(), new Date(), new Date(), TrainingStatus.RESERVED.getName()));
        trainingList.add(new TrainingDto("Kolejne fajne szkolenie", "ITMAGINATION", 12, new Date(), new Date(), new Date(), TrainingStatus.RESERVED.getName()));

        indDto.setProducts(productDtoList);
        indDto.setTrainings(trainingList);
        indDto.setAgreements(agreementList);

        return indDto;
    }
}