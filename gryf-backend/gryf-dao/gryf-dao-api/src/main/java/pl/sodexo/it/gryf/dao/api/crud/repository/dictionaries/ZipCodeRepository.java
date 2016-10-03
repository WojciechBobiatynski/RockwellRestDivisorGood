package pl.sodexo.it.gryf.dao.api.crud.repository.dictionaries;

import pl.sodexo.it.gryf.common.dto.zipcodes.searchform.ZipCodeSearchQueryDTO;
import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface ZipCodeRepository extends GenericRepository<ZipCode, Long> {

    List<ZipCode> findZipCodes(ZipCodeSearchQueryDTO zipCode);
}
