package pl.sodexo.it.gryf.web.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;
import pl.sodexo.it.gryf.service.api.other.ApplicationParameters;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class AuthModelAttribute {
    
    @Autowired
    ApplicationParameters aps;
    
    private final ObjectMapper om = new ObjectMapper();
     
    @ModelAttribute("privileges")
    public String privilegesJson() throws JsonProcessingException {
        Map<String, Boolean> res = new HashMap<>();
        Collection<? extends GrantedAuthority> auths = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority auth : auths) {
            res.put(auth.getAuthority(), true);
        }
        return om.writeValueAsString(res);
    }

    @ModelAttribute("login")
    public String loginString() {
        GryfUser loggedUser = GryfUser.getLoggedUser();
        if(loggedUser == null) return "";
        return loggedUser.getUserLogin();        
    }

    @ModelAttribute("loggedIn")
    public boolean loggedInBool() {
        return !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
    }

    @ModelAttribute("cdnUrl")    
    public String cdnUrlString(){
        return aps.getCdnUrl();
    }
    
    @ModelAttribute("resourcesUrl")    
    public String resourcesUrlString(){
        return aps.getResourcesUrl();
    }

    @ModelAttribute("jsUrl")
    public String jsUrlString(){
        return aps.getJsUrl();
    }

    @ModelAttribute("templatesUrl")
    public String templatesString(){
        return aps.getTemplatesUrl();
    }

    @ModelAttribute("attachmentMaxSize")
    public int attachmentMaxSize() {
        return aps.getAttachmentMaxSize();
    }

    @ModelAttribute("sessionTimeout")
    public int getSessionTimeout() {
        return aps.getSessionTimeout() * 60 * 1000;
    }
}
