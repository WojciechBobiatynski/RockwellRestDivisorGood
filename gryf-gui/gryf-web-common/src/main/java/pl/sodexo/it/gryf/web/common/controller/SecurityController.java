package pl.sodexo.it.gryf.web.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.sodexo.it.gryf.common.dto.security.RoleDto;
import pl.sodexo.it.gryf.service.api.security.SecurityService;

import java.util.List;

import static pl.sodexo.it.gryf.web.common.util.PageConstant.PATH_SECURITY;
import static pl.sodexo.it.gryf.web.common.util.PageConstant.PATH_TI_USER_ROLES;

/**
 * Kontroler związany z bezpieczeństwem
 *
 * Created by akmiecinski on 08.11.2016.
 */
@Controller
@RequestMapping(value = PATH_SECURITY)
public class SecurityController {

    @Autowired
    private SecurityService securityService;

    @RequestMapping(value = PATH_TI_USER_ROLES, method = RequestMethod.GET)
    @ResponseBody
    public List<RoleDto> getRolesForTiUser() {
        return securityService.findRolesForTiUser();
    }

}
