package pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.individuals.searchform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.dictionaries.zipcodes.searchform.ZipCodeSearchResultDTO;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.detailsForm.IndividualDto;
import pl.sodexo.it.gryf.common.dto.publicbenefits.individuals.searchform.IndividualSearchResultDTO;
import pl.sodexo.it.gryf.model.publicbenefits.individuals.Individual;
import pl.sodexo.it.gryf.service.mapping.entityToDto.GryfEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.VersionableEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.dictionaries.ZipCodeEntityMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.dictionaries.searchform.ZipCodeEntityToSearchResultMapper;
import pl.sodexo.it.gryf.service.mapping.entityToDto.publicbenefits.individuals.IndividualContactEntityMapper;

/**
 * Maper mapujący encję Individual na dto  IndividualDto
 *
 * Created by jbentyn on 2016-09-27.
 */
@Component
public class IndividualEntityToSearchResultMapper extends GryfEntityMapper<Individual, IndividualSearchResultDTO> {

    @Autowired
    private ZipCodeEntityToSearchResultMapper zipCodeEntityToSearchResultMapper;

    @Override
    protected IndividualSearchResultDTO initDestination() {
        return new IndividualSearchResultDTO();
    }

    @Override
    public void map(Individual entity, IndividualSearchResultDTO dto) {
        super.map(entity, dto);
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPesel(entity.getPesel());
        dto.setDocumentNumber(entity.getDocumentNumber());
        dto.setDocumentType(entity.getDocumentType());
        dto.setAddressInvoice(entity.getAddressInvoice());
        dto.setZipCodeInvoice(zipCodeEntityToSearchResultMapper.convert(entity.getZipCodeInvoice()));
        dto.setAddressCorr(entity.getAddressCorr());
        dto.setZipCodeCorr(zipCodeEntityToSearchResultMapper.convert(entity.getZipCodeCorr()));
        dto.setId(entity.getId());
    }
}
