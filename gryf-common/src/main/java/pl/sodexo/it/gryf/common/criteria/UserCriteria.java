package pl.sodexo.it.gryf.common.criteria;

import pl.sodexo.it.gryf.common.annotation.ConfigInjectable;
import pl.sodexo.it.gryf.common.api.ConfigSettable;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;

import java.io.Serializable;

/**
 * Kryteria kontekstu zalogowanego użytkownika
 *
 * Created by akmiecinski on 24.10.2016.
 */
@ConfigInjectable
public class UserCriteria implements Serializable, ConfigSettable {

    private static final long serialVersionUID = 1L;

    // udostepnienie ApplicationParameters - jako object ze wzgledu na modulowosc
    private transient Object config;

    public GryfUser getGryfUser() {
        return GryfUser.getLoggedUser();
    }

    public Object getConfig() {
        return config;
    }

    @Override
    public void setConfig(Object config) {
        this.config = config;
    }
}
