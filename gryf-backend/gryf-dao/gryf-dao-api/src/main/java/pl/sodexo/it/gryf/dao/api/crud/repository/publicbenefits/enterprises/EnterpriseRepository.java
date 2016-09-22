package pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.enterprises;

import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.searchform.EnterpriseSearchQueryDTO;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
import pl.sodexo.it.gryf.dao.api.crud.repository.GenericRepository;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface EnterpriseRepository extends GenericRepository<Enterprise, Long> {

    List<Enterprise> findByVatRegNum(String vatRegNum);

    Enterprise getForUpdate(Long id);

    List<Enterprise> findEnterprises(EnterpriseSearchQueryDTO dto);
}
