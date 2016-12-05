package pl.sodexo.it.gryf.service.api.publicbenefits.enterprises;

import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.detailsform.EnterpriseDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.searchform.EnterpriseSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.searchform.EnterpriseSearchResultDTO;

import java.util.List;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface EnterpriseService {

    EnterpriseDto findEnterprise(Long id);

    List<EnterpriseSearchResultDTO> findEnterprises(EnterpriseSearchQueryDTO enterprise);

    EnterpriseDto createEnterprise();

    Long saveEnterpriseDto(EnterpriseDto enterpriseDto, boolean checkVatRegNumDup, boolean validateAccountRepayment);

    void updateEnterpriseDto(EnterpriseDto enterpriseDto, boolean checkVatRegNumDup, boolean validateAccountRepayment);
}
