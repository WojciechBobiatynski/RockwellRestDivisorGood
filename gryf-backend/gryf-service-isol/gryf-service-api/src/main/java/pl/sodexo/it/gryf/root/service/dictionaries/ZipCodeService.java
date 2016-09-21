package pl.sodexo.it.gryf.root.service.dictionaries;

import pl.sodexo.it.gryf.common.dto.dictionaries.zipcodes.searchform.ZipCodeSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.dictionaries.zipcodes.searchform.ZipCodeSearchResultDTO;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-19.
 */
public interface ZipCodeService {

    ZipCode findZipCode(Long id);

    List<ZipCodeSearchResultDTO> findZipCodes(ZipCodeSearchQueryDTO zipCode);

    ZipCode createZipCode();

    void saveZipCode(ZipCode zipCode);

    void updateZipCode(ZipCode zipCode);
}
