package pl.sodexo.it.gryf.web.ti.controller.password.retrieve;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.common.dto.captcha.CaptchaResponseDto;
import pl.sodexo.it.gryf.common.exception.GryfRuntimeException;
import pl.sodexo.it.gryf.common.exception.authentication.GryfUserNotActiveException;
import pl.sodexo.it.gryf.common.exception.verification.GryfCaptchaException;
import pl.sodexo.it.gryf.common.exception.verification.GryfVerificationException;
import pl.sodexo.it.gryf.service.api.security.VerificationService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

import static pl.sodexo.it.gryf.common.utils.GryfConstants.*;
import static pl.sodexo.it.gryf.web.common.util.PageUtil.getURLWithContextPath;
import static pl.sodexo.it.gryf.web.ti.util.TiPageConstant.*;

/**
 * Kontroler obsługjący zdarzenia związane z odzyskaniem hasła dla Usługodawcy
 *
 * Created by akmiecinski on 25.10.2016.
 */
@Controller
public class RetrievePasswordController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetrievePasswordController.class);
    private static final String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify";

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private ApplicationParameters applicationParameters;

    @RequestMapping(value = PATH_RETRIEVE_PASSWORD, method = RequestMethod.GET)
    public String retrievePassword(Model uiModel) {
        uiModel.addAttribute(JSP_SITE_KEY_PLACEHOLDER, applicationParameters.getPublicCaptchaKey());
        return PAGE_RETRIEVE_PASSWORD;
    }

    @RequestMapping(value = PATH_RETRIEVE_RESET_PASSWORD, method = RequestMethod.POST)
    public String resetPassword(HttpServletRequest request, Model uiModel) {
        String email = request.getParameter("email");
        if(!checkCaptcha(request)){
            uiModel.addAttribute(JSP_ERROR_PLACEHOLDER, new GryfCaptchaException("Nie zaznaczono captchy"));
            uiModel.addAttribute(JSP_SITE_KEY_PLACEHOLDER, applicationParameters.getPublicCaptchaKey());
            return PAGE_RETRIEVE_PASSWORD;
        };
        return resetAndGetComeBackUrl(uiModel, email);
    }

    private String resetAndGetComeBackUrl(Model uiModel, String email) {
        String comebackPage;
        try {
            verificationService.resetTiUserPassword(email);
            uiModel.addAttribute(JSP_EMAIL_PLACEHOLDER, email);
            comebackPage = PAGE_RETRIEVE_RESET_PASSWORD_SUCCESS;
        }
        catch(GryfUserNotActiveException | GryfVerificationException e){
            comebackPage = PAGE_RETRIEVE_RESET_PASSWORD_SUCCESS;
        }catch (GryfRuntimeException e){
            LOGGER.error("Blad podczas obslugi", e);
            uiModel.addAttribute(JSP_ERROR_PLACEHOLDER, e);
            uiModel.addAttribute(JSP_SITE_KEY_PLACEHOLDER, applicationParameters.getPublicCaptchaKey());
            comebackPage = PAGE_RETRIEVE_PASSWORD;
        } catch(Exception e) {
            LOGGER.error("Blad podczas obslugi", e);
            uiModel.addAttribute(JSP_UNKNOWNERROR_PLACEHOLDER, e);
            comebackPage = PAGE_RETRIEVE_PASSWORD;
        }
        return comebackPage;
    }

    private boolean checkCaptcha(HttpServletRequest request){
        RestTemplate rest = new RestTemplate();

        HttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
        HttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        HttpMessageConverter httpJsonConverter = new MappingJackson2HttpMessageConverter();
        rest.setMessageConverters(Arrays.asList(formHttpMessageConverter, stringHttpMessageConverter, httpJsonConverter));

        String captcha_response = request.getParameter(JSP_G_RECAPTCHA_RESPONSE_PLACEHOLDER);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(JSP_CAPTCHA_SECRET_PARAM_PLACEHOLDER, applicationParameters.getSecretCaptchaKey());
        map.add(JSP_CAPTCHA_RESPONSE_PARAM_PLACEHOLDER, captcha_response);
        CaptchaResponseDto result = rest.postForObject(CAPTCHA_URL, map, CaptchaResponseDto.class);
        return result.isSuccess();
    }
}
