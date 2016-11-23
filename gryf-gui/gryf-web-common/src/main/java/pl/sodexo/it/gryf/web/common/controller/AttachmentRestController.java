package pl.sodexo.it.gryf.web.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.sodexo.it.gryf.common.dto.other.GrantProgramAttachmentTypeDto;
import pl.sodexo.it.gryf.service.api.other.AttachmentService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;

import java.util.List;

import static pl.sodexo.it.gryf.web.common.util.PageConstant.PATH_ATT_TYPES;
import static pl.sodexo.it.gryf.web.common.util.PageConstant.PATH_REST_ATTACHMENTS;

/**
 * Kontroler do operacji na załącznikach
 *
 * Created by akmiecinski on 23.11.2016.
 */
@Controller
@RequestMapping(value = PATH_REST_ATTACHMENTS)
public class AttachmentRestController {

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private SecurityChecker securityChecker;

    @RequestMapping(value = PATH_ATT_TYPES + "/{grantProgramId}", method = RequestMethod.GET)
    @ResponseBody
    public List<GrantProgramAttachmentTypeDto> getRolesForTiUser(@PathVariable Long grantProgramId) {
//        securityChecker
        return attachmentService.findAttachmentTypesByGrantProgramId(grantProgramId);
    }

}
