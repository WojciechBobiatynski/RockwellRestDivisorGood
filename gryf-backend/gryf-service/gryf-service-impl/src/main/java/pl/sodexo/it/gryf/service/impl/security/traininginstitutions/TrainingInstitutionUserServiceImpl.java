package pl.sodexo.it.gryf.service.impl.security.traininginstitutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.dao.api.search.mapper.SecuritySearchMapper;
import pl.sodexo.it.gryf.service.api.security.traininginstitutions.TrainingInstitutionUserService;

import java.util.List;

/**
 * Created by jbentyn on 2016-10-04.
 */
@Service
public class TrainingInstitutionUserServiceImpl implements TrainingInstitutionUserService{

    @Autowired
    private SecuritySearchMapper securitySearchMapper;

    @Override
    public List<String> findPrivilegesForLogin(String login) {
        return securitySearchMapper.findTIUserPrivileges(login);
    }
}
