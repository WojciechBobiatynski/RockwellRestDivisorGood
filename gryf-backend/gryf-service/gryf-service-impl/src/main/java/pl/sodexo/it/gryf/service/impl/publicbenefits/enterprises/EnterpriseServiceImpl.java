package pl.sodexo.it.gryf.service.impl.publicbenefits.enterprises;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.detailsform.EnterpriseDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.searchform.EnterpriseSearchQueryDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.enterprises.searchform.EnterpriseSearchResultDTO;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.enterprises.EnterpriseRepository;
import pl.sodexo.it.gryf.model.publicbenefits.enterprises.Enterprise;
import pl.sodexo.it.gryf.service.api.publicbenefits.enterprises.EnterpriseService;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.enterprises.EnterpriseServiceLocal;
import pl.sodexo.it.gryf.service.mapping.dtoToEntity.publicbenefits.enterprises.EnterpriseDtoMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.enterprises.EnterpriseEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.enterprises.searchform.EnterpriseEntityToSearchResultMapper;

import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-06-22.
 */
@Service
@Transactional
public class EnterpriseServiceImpl implements EnterpriseService {

    //FIELDS

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Autowired
    private EnterpriseEntityMapper enterpriseEntityMapper;

    @Autowired
    private EnterpriseDtoMapper enterpriseDtoMapper;

    @Autowired
    private EnterpriseServiceLocal enterpriseServiceLocal;

    @Autowired
    private EnterpriseEntityToSearchResultMapper enterpriseEntityToSearchResultMapper;

    //PUBLIC METHODS

    @Override
    public EnterpriseDto findEnterprise(Long id) {
        EnterpriseDto dto = enterpriseEntityMapper.convert( enterpriseRepository.getForUpdate(id));
        return dto;
    }

    @Override
    public List<EnterpriseSearchResultDTO> findEnterprises(EnterpriseSearchQueryDTO enterprise) {
        List<Enterprise> enterprises = enterpriseRepository.findEnterprises(enterprise);
        return enterpriseEntityToSearchResultMapper.convert(enterprises);
    }

    @Override
    public EnterpriseDto createEnterprise() {
        return new EnterpriseDto();
    }

    @Override
    public Long saveEnterpriseDto(EnterpriseDto enterpriseDto, boolean checkVatRegNumDup) {
        Enterprise enterprise = enterpriseServiceLocal.saveEnterprise(enterpriseDtoMapper.convert(enterpriseDto),checkVatRegNumDup);
        return enterprise.getId();
    }

    @Override
    public void updateEnterpriseDto(EnterpriseDto enterpriseDto, boolean checkVatRegNumDup) {
        Enterprise enterprise = enterpriseDtoMapper.convert(enterpriseDto);
        enterpriseServiceLocal.updateEnterprise(enterprise,checkVatRegNumDup);
    }

}
