package pl.sodexo.it.gryf.service.impl.dictionaries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.dictionaries.zipcodes.searchform.ZipCodeSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.dictionaries.zipcodes.searchform.ZipCodeSearchResultDTO;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.dao.api.crud.repository.dictionaries.ZipCodeRepository;
import pl.sodexo.it.gryf.service.api.dictionaries.ZipCodeService;
import pl.sodexo.it.gryf.service.local.api.ValidateService;

import java.util.List;

@Service
@Transactional
public class ZipCodeServiceImpl implements ZipCodeService {

    //FIELDS

    @Autowired
    private ValidateService validateService;

    @Autowired
    private ZipCodeRepository zipCodeRepository;

    //PUBLIC METHODS

    @Override
    public ZipCode findZipCode(Long id) {
        return zipCodeRepository.get(id);
    }

    @Override
    public List<ZipCodeSearchResultDTO> findZipCodes(ZipCodeSearchQueryDTO zipCode) {
        List<ZipCode> zipCodes = zipCodeRepository.findZipCodes(zipCode);
        return ZipCodeSearchResultDTO.createList(zipCodes);
    }

    @Override
    public ZipCode createZipCode() {
        ZipCode zipCode = new ZipCode();
        zipCode.setActive(Boolean.TRUE);
        return zipCode;
    }

    @Override
    public void saveZipCode(ZipCode zipCode) {
        validateService.validate(zipCode);
        toUpperCityName(zipCode);
        zipCodeRepository.save(zipCode);
    }

    @Override
    public void updateZipCode(ZipCode zipCode) {
        validateService.validate(zipCode);
        toUpperCityName(zipCode);
        zipCodeRepository.update(zipCode, zipCode.getId());
    }

    //PRIVATE METHODS

    private void toUpperCityName(ZipCode zipCode){
        if(zipCode.getCityName() != null){
            zipCode.setCityName(zipCode.getCityName().toUpperCase());
        }
    }

}
