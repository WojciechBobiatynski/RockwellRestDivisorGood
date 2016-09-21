package pl.sodexo.it.gryf.root.service.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.sodexo.it.gryf.common.Privileges;
import pl.sodexo.it.gryf.common.exception.AuthAssertionFailureException;
import pl.sodexo.it.gryf.common.exception.AuthAssertionFailureFormException;
import pl.sodexo.it.gryf.common.validation.InsertablePrivilege;
import pl.sodexo.it.gryf.root.service.SecurityCheckerService;

import java.lang.reflect.Field;
import java.util.Collection;

@Service
public class SecurityCheckerServiceImpl implements SecurityCheckerService {

    //PUBLIC METHODS - ASSERT PRIVILEGE

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
