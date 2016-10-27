package pl.sodexo.it.gryf.dao.impl.crud.repository.publicbenefits.contracts;

import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.dao.api.crud.repository.publicbenefits.contracts.ContractRepository;
import pl.sodexo.it.gryf.dao.impl.crud.repository.GenericRepositoryImpl;
import pl.sodexo.it.gryf.model.publicbenefits.contracts.Contract;

/**
 * Created by Isolution on 2016-10-27.
 */
@Repository
public class ContractRepositoryImpl extends GenericRepositoryImpl<Contract, Long> implements ContractRepository {

}

