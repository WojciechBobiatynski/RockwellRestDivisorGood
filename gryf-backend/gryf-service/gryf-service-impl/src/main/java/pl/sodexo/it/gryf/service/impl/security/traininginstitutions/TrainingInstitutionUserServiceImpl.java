package pl.sodexo.it.gryf.service.impl.security.traininginstitutions;

import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.service.api.security.traininginstitutions.TrainingInstitutionUserService;

import java.util.List;

/**
 * Created by jbentyn on 2016-10-04.
 */
@Service
public class TrainingInstitutionUserServiceImpl implements TrainingInstitutionUserService{

    @Override
    public List<String> findRolesForLogin(String login, String password) {
        return null;
    }
}
