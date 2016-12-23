package pl.sodexo.it.gryf.service.impl.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.common.exception.AuthAssertionFailureException;
import pl.sodexo.it.gryf.common.exception.AuthAssertionFailureFormException;
import pl.sodexo.it.gryf.common.validation.InsertablePrivilege;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ElectronicReimbursementsService;
import pl.sodexo.it.gryf.service.api.publicbenefits.electronicreimbursements.ErmbsAttachmentService;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingInstanceService;
import pl.sodexo.it.gryf.service.api.publicbenefits.traininginstiutions.TrainingService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;
import pl.sodexo.it.gryf.service.local.api.GryfValidator;

import java.lang.reflect.Field;
import java.util.Collection;

@Component
//todo do przerobienia
public class SecurityCheckerImpl implements SecurityChecker {

    //PUBLIC METHODS - ASSERT PRIVILEGE

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private TrainingInstanceService trainingInstanceService;

    @Autowired
    private ElectronicReimbursementsService electronicReimbursementsService;

    @Autowired
    private ErmbsAttachmentService ermbsAttachmentService;

    @Autowired
    private GryfValidator gryfValidator;

    //PUBLIC METHODS - ASSERT

    @Override
    public void assertServicePrivilege(Privileges... privileges) {
        if (!hasPrivilege(privileges)) {
            throw new AuthAssertionFailureException(privileges);
        }
    }

    @Override
    public void assertFormPrivilege(Privileges... privileges) {
        if (!hasPrivilege(privileges)) {
            throw new AuthAssertionFailureFormException(privileges);
        }
    }

    //PUBLIC METHODS - ASSERT OBJECT ACCESS

    @Override
    public void assertTiUserAccessTraining(Long trainingId) {
        if(trainingId != null){
            if(!trainingService.isTrainingInLoggedUserInstitution(trainingId)){
                gryfValidator.validate("Nie masz dostepu do danego szkolenia");
            }
        }
    }

    @Override
    public void assertTiUserAccessTrainingInstance(Long trainingInstanceId){
        if(trainingInstanceId != null){
            if(!trainingInstanceService.isTrainingInstanceInLoggedUserInstitution(trainingInstanceId)){
                gryfValidator.validate("Nie masz dostepu do danej instancji szkolenia");
            }
        }
    }

    @Override
    public void assertTiUserAccessEreimbursement(Long ereimbursementId){
        if(ereimbursementId != null){
            if(!electronicReimbursementsService.isEreimbursementInLoggedUserInstitution(ereimbursementId)){
                gryfValidator.validate("Nie masz dostepu do danego rozliczenia");
            }
        }
    }

    @Override
    public void assertTiUserAccessEreimbursementAttachment(Long ereimbursementAttachmentId){
        if(ereimbursementAttachmentId != null){
            if(!ermbsAttachmentService.isEreimbursementAttachmentInLoggedUserInstitution(ereimbursementAttachmentId)){
                gryfValidator.validate("Nie masz dostepu do danego załącznika rozliczenia");
            }
        }
    }

    //PUBLIC METHODS - HAS PRIVILEGE

    @Override
    public boolean hasPrivilege(String privilege) {
        Collection<? extends GrantedAuthority> auths = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority auth : auths) {
            if (auth.getAuthority().equals(privilege)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasPrivilege(Privileges... privileges) {
        for (int i = 0; i < privileges.length; i++) {
            if(hasPrivilege(privileges[i].name())){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasInsertablePrivilege(Field field){
        InsertablePrivilege insertablePrivilege = field.getAnnotation(InsertablePrivilege.class);
        if(insertablePrivilege != null) {
            if (insertablePrivilege.privileges().length > 0) {
                return hasPrivilege(insertablePrivilege.privileges());
            }
        }
        return true;
    }

    //PUBLIC METHODS - OTHERS

    @Override
    public String getInsertablePrivilegeMessage(Field field){
        InsertablePrivilege insertablePrivilege = field.getAnnotation(InsertablePrivilege.class);
        if(insertablePrivilege != null) {
            return insertablePrivilege.message();
        }
        return null;
    }
}
