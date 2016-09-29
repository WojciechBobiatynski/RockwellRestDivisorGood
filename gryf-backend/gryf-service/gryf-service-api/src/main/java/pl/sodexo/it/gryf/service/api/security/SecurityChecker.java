package pl.sodexo.it.gryf.service.api.security;

import pl.sodexo.it.gryf.common.Privileges;

import java.lang.reflect.Field;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface SecurityChecker {

    void assertServicePrivilege(Privileges... privileges);

    void assertFormPrivilege(Privileges... privileges);

    boolean hasPrivilege(String privilege);

    boolean hasPrivilege(Privileges... privileges);

    boolean hasInsertablePrivilege(Field field);

    String getInsertablePrivilegeMessage(Field field);
}
