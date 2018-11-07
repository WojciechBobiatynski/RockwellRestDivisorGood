package pl.sodexo.it.gryf.service.impl.generator;

import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.dto.publicbenefits.importdata.ImportOrderDTO;
import pl.sodexo.it.gryf.common.generator.IdentityGenerator;
import pl.sodexo.it.gryf.service.api.generator.IdentityGeneratorService;

import java.util.Objects;

/**
 * Generator identyfikatora Zamówienia
 *
 */
@Component(IdentityGeneratorService.ORDER_IDENTITY_GENERATOR_CONTRACT_ID)
public class OrderIdentityGeneratorService implements IdentityGeneratorService<ImportOrderDTO, String> {
    @Override
    public IdentityGenerator getGenerator(ImportOrderDTO importOrderDTO) {

        return (IdentityGenerator<ImportOrderDTO, String>) context -> {
            if (Objects.nonNull(context)) {
                    String[] tab = context.getExternalOrderId().split("/");
                    return  tab[1];
            }
            return null;
        };
    }
}
