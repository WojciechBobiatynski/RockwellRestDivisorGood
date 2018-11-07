package pl.sodexo.it.gryf.service.impl.generator;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportContractDTO;
import pl.sodexo.it.gryf.common.generator.IdentityGenerator;
import pl.sodexo.it.gryf.service.api.generator.IdentityGeneratorService;

import java.util.Objects;

/**
 * Generator identyfikatora dla Umowy
 *
 */
@Component(IdentityGeneratorService.CONTRACT_IDENTITY_GENERATOR_CONTRACT_ID)
public class ContractIdentityGeneratorService implements IdentityGeneratorService<ImportContractDTO, String> {
    @Override
    public IdentityGenerator getGenerator(ImportContractDTO importContractDTO) {

        return (IdentityGenerator<ImportContractDTO, String>) context -> {
            if (Objects.nonNull(context)) {
                    String[] tab = context.getExternalOrderId().split("/");
                    return  tab[1];
            }
            return null;
        };
    }
}
