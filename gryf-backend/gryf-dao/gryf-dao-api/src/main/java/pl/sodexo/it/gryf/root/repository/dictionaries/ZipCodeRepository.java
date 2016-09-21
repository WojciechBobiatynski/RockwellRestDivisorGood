package pl.sodexo.it.gryf.root.repository.dictionaries;

import pl.sodexo.it.gryf.common.dto.dictionaries.zipcodes.searchform.ZipCodeSearchQueryDTO;
import pl.sodexo.it.gryf.model.dictionaries.ZipCode;
import pl.sodexo.it.gryf.root.repository.GenericRepository;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface ZipCodeRepository extends GenericRepository<ZipCode, Long> {

    List<ZipCode> findZipCodes(ZipCodeSearchQueryDTO zipCode);
}
