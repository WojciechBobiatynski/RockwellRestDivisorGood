package pl.sodexo.it.gryf.web.fo.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;

@ControllerAdvice
public class AuthModelAttribute {

    private final ObjectMapper om = new ObjectMapper();
    
    @Autowired
    private ApplicationParameters aps;
     
    @ModelAttribute("privileges")
    public String privileges() throws JsonProcessingException {
        return om.writeValueAsString(GryfUser.getLoggedUserauthorities());
    }

    @ModelAttribute("login")
    public String login() {
        return GryfUser.getLoggedUserLogin();        
    }

    @ModelAttribute("loggedIn")
    public boolean isLoggedIn() {
        return GryfUser.isUserLoggedInApplication();
    }

    @ModelAttribute("cdnUrl")    
    public String cdnUrl(){
        return aps.getCdnUrl();
    }

    @ModelAttribute("cdnTitle")
    public String cdnTitle(){
        return aps.getCdnTitle();
    }

    @ModelAttribute("resourcesUrl")    
    public String resourcesUrl(){
        return aps.getResourcesUrl();
    }

    @ModelAttribute("jsUrl")
    public String jsUrl(){
        return aps.getJsUrl();
    }

    @ModelAttribute("templatesUrl")
    public String templatesUrl(){
        return aps.getTemplatesUrl();
    }

    @ModelAttribute("attachmentMaxSize")
    public int attachmentMaxSize() {
        return aps.getAttachmentMaxSize();
    }

    @ModelAttribute("sessionTimeout")
    public int sessionTimeout() {
        return aps.getSessionTimeout() * 60 * 1000;
    }
}
