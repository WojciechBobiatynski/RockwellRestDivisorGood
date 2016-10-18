package pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.traininginstiutions.searchform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.traininginstiutions.searchform.TrainingInstitutionSearchResultDTO;
import pl.sodexo.it.gryf.model.publicbenefits.traininginstiutions.TrainingInstitution;
import pl.sodexo.it.gryf.service.mapping.entityToDto.GryfEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.dictionaries.searchform.ZipCodeEntityToSearchResultMapper;

/**
 * Mapper mapujący encję TrainingInstitution na dto TrainingInstitutionDto
 *
 * Created by jbentyn on 2016-09-27.
 */
@Component
public class TrainingInstitutionEntityToSearchResultMapper extends GryfEntityMapper<TrainingInstitution, TrainingInstitutionSearchResultDTO> {

    @Autowired
    private ZipCodeEntityToSearchResultMapper zipCodeEntityToSearchResultMapper;

    @Override
    protected TrainingInstitutionSearchResultDTO initDestination() {
        return new TrainingInstitutionSearchResultDTO();
    }

    @Override
    public void map(TrainingInstitution entity, TrainingInstitutionSearchResultDTO dto) {
        super.map(entity, dto);
        dto.setName(entity.getName());
        dto.setVatRegNum(entity.getVatRegNum());
        dto.setAddressInvoice(entity.getAddressInvoice());
        dto.setZipCodeInvoice(zipCodeEntityToSearchResultMapper.convert(entity.getZipCodeInvoice()));
        dto.setAddressCorr(entity.getAddressCorr());
        dto.setZipCodeCorr(zipCodeEntityToSearchResultMapper.convert(entity.getZipCodeCorr()));
        dto.setId(entity.getId());
    }
}
