package pl.sodexo.it.gryf.service.impl.dictionaries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.dictionaries.zipcodes.detailsform.ZipCodeDto;
import pl.sodexo.it.gryf.common.dto.dictionaries.zipcodes.searchform.ZipCodeSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.dictionaries.zipcodes.searchform.ZipCodeSearchResultDTO;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.dao.api.crud.repository.dictionaries.ZipCodeRepository;
import pl.sodexo.it.gryf.service.api.dictionaries.ZipCodeService;
import pl.sodexo.it.gryf.service.local.api.ValidateService;
import pl.sodexo.it.gryf.service.mapping.dtoToEntity.dictionaries.ZipCodeDtoMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.dictionaries.ZipCodeEntityMapper;

import java.util.List;

@Service
@Transactional
public class ZipCodeServiceImpl implements ZipCodeService {

    //FIELDS

    @Autowired
    private ValidateService validateService;

    @Autowired
    private ZipCodeRepository zipCodeRepository;

    @Autowired
    private ZipCodeEntityMapper zipCodeEntityMapper;

    @Autowired
    private ZipCodeDtoMapper zipCodeDtoMapper;

    //PUBLIC METHODS

    @Override
    public ZipCodeDto findZipCode(Long id) {
        ZipCode zipCode = zipCodeRepository.get(id);
        return zipCodeEntityMapper.convert(zipCode);
    }

    @Override
    public List<ZipCodeSearchResultDTO> findZipCodes(ZipCodeSearchQueryDTO zipCode) {
        List<ZipCode> zipCodes = zipCodeRepository.findZipCodes(zipCode);
        return ZipCodeSearchResultDTO.createList(zipCodes);
    }

    @Override
    public ZipCodeDto createZipCode() {
        ZipCode zipCode = new ZipCode();
        zipCode.setActive(Boolean.TRUE);
        return  zipCodeEntityMapper.convert(zipCode);
    }

    @Override
    public void saveZipCode(ZipCodeDto zipCodeDto) {
        ZipCode zipCode = zipCodeDtoMapper.convert(zipCodeDto);
        validateService.validate(zipCode);
        toUpperCityName(zipCode);
        zipCodeRepository.save(zipCode);
    }

    @Override
    public void updateZipCode(ZipCodeDto zipCodeDto) {
        ZipCode zipCode = zipCodeDtoMapper.convert(zipCodeDto);
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
