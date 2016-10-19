package pl.sodexo.it.gryf.common.user;

import pl.sodexo.it.gryf.common.dto.user.GryfFoUser;
import pl.sodexo.it.gryf.common.dto.user.GryfIndUser;
import pl.sodexo.it.gryf.common.dto.user.GryfTiUser;

/**
 * Interfejs do implementacji wzorca visitor dla użytkownikow
 *
 * Created by jbentyn on 2016-10-19.
 */
public interface UserVisitor<T> {

    T visitFo(GryfFoUser gryfFoUser);

    T visitInd(GryfIndUser gryfIndUser);

    T visitTi(GryfTiUser gryfTiUser);
}
