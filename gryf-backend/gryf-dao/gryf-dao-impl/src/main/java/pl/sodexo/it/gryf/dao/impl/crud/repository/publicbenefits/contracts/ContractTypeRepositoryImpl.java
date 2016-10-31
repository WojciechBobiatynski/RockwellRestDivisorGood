package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.contracts;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.contracts.ContractTypeRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.ContractType;

/**
 * Created by adziobek on 31.10.2016.
 */
@Repository
public class ContractTypeRepositoryImpl extends GenericRepositoryImpl<ContractType, String> implements ContractTypeRepository {

}