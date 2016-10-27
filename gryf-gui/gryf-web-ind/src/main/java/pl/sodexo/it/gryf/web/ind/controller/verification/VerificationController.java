package pl.sodexo.it.gryf.web.ind.controller.verification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.sodexo.it.gryf.common.dto.security.individuals.VerificationDto;
import pl.sodexo.it.gryf.common.exception.GryfRuntimeException;
import pl.sodexo.it.gryf.service.api.security.VerificationService;

import javax.servlet.http.HttpServletRequest;

import static pl.sodexo.it.gryf.web.ind.util.IndPageConstant.*;

/**
 * Kontroler obsługujący akcje związane z weryfikacją
 *
 * Created by akmiecinski on 19.10.2016.
 */
@Controller
public class VerificationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerificationController.class);

    @Autowired
    private VerificationService verificationService;

    @RequestMapping(value = PATH_VERIFICATION, method = RequestMethod.GET)
    public String remindVerificationCodePage() {
        return PAGE_VERIFICATION;
    }

    @RequestMapping(value = PATH_VERIFICATION_RESEND_CODE, method = RequestMethod.POST)
    public String resendVerificationCode(HttpServletRequest request, Model uiModel) {
        String pesel = request.getParameter("pesel");
        String email = request.getParameter("email");
        LOGGER.info("resendVerificationCode, PESEL={}, email={}",pesel, email);

        VerificationDto verificationDto = new VerificationDto(pesel, email);
        return sendAndGetComeBackUrl(uiModel, verificationDto);
    }

    private String sendAndGetComeBackUrl(Model uiModel, VerificationDto verificationDto) {
        String comebackPage;
        try {
            verificationService.resendVerificationCode(verificationDto);
            comebackPage = PAGE_VERIFICATION_RESEND_SUCCESS;
        } catch (GryfRuntimeException e){
            LOGGER.error("Blad podczas obslugi", e);
            uiModel.addAttribute("error", e);
            comebackPage = PAGE_VERIFICATION;
        } catch(Exception e) {
            LOGGER.error("Blad podczas obslugi", e);
            uiModel.addAttribute("unknowerror", e);
            comebackPage = PAGE_VERIFICATION;
        }
        return comebackPage;
    }
}
