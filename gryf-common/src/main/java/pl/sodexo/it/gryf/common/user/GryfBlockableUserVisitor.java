package pl.sodexo.it.gryf.common.user;

import pl.sodexo.it.gryf.common.dto.security.individuals.GryfIndUserDto;
import pl.sodexo.it.gryf.common.dto.security.trainingInstitutions.GryfTiUserDto;

/**
 * Created by akmiecinski on 24.10.2016.
 */
public interface GryfBlockableUserVisitor <T> {

    T visitInd(GryfIndUserDto gryfIndUserDto);

    T visitTi(GryfTiUserDto gryfTiUserDto);
}
