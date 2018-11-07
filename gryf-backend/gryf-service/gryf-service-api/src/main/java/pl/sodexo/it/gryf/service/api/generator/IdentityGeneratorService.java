package pl.sodexo.it.gryf.service.api.generator;

import pl.sodexo.it.gryf.common.generator.IdentityGenerator;

/**
 *  Intefejs usługi wyboru generatora
 *  identyfikatora obiektu P
 *
 * @param <P>
 */
public interface IdentityGeneratorService<P, R> {

    String CONTRACT_IDENTITY_GENERATOR_CONTRACT_ID = "ContractIdentityGeneratorService";

    String ORDER_IDENTITY_GENERATOR_CONTRACT_ID = "OrderContractIdentityGeneratorService";

    IdentityGenerator<P,R> getGenerator(P object);

}
