package pl.sodexo.it.gryf.service.api.security;

import pl.sodexo.it.gryf.common.enums.Privileges;

import java.lang.reflect.Field;

/**
 * Created by jbentyn on 2016-09-20.
 */
public interface SecurityChecker{

    //PUBLIC METHODS - ASSERT

    void assertServicePrivilege(Privileges... privileges);

    void assertFormPrivilege(Privileges... privileges);

    //PUBLIC METHODS - ASSERT OBJECT ACCESS

    void assertTiUserAccessTraining(Long trainingId);

    void assertTiUserAccessTrainingInstance(Long trainingInstanceId);

    void assertTiUserAccessEreimbursement(Long ereimbursementId);

    void assertTiUserAccessEreimbursementAttachment(Long ereimbursementAttachmentId);

    void assertIndUserAccessTrainingInstance(Long trainingInstanceId);

    //PUBLIC METHODS - HAS PRIVILEGE

    boolean hasPrivilege(String privilege);

    boolean hasPrivilege(Privileges... privileges);

    boolean hasInsertablePrivilege(Field field);

    //PUBLIC METHODS - OTHERS

    String getInsertablePrivilegeMessage(Field field);

}
