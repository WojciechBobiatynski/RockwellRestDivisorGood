package pl.sodexo.it.gryf.web.ind.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.sodexo.it.gryf.common.dto.security.VerificationDto;
import pl.sodexo.it.gryf.common.exception.GryfRuntimeException;
import pl.sodexo.it.gryf.common.exception.verification.GryfVerificationException;
import pl.sodexo.it.gryf.service.api.security.VerificationService;

import javax.servlet.http.HttpServletRequest;

import static pl.sodexo.it.gryf.web.common.util.PageUtil.setUpMainContent;
import static pl.sodexo.it.gryf.web.ind.util.IndPageConstant.*;

/**
 * Kontroler widokow glownych dla IND.
 * 
 * Created by akuchna on 2016-10-14.
 */
@Controller
@RequestMapping(PATH_MAIN)
public class IndMainViewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndMainViewController.class);

    @Autowired
    private VerificationService verificationService;

    @RequestMapping(value = PATH_IND_LOGIN, method = RequestMethod.GET)
    public String login() {
        return PAGE_IND_LOGIN;
    }

    @RequestMapping(PATH_MAIN)
    public String welcome(Model model) {
        setUpMainContent(model, SUB_PAGE_IND_WELCOME);
        return PAGE_MAIN;
    }

    @RequestMapping(value = PATH_VERIFICATION, method = RequestMethod.GET)
    public String remindVerificationCodePage() {
        return PAGE_VERIFICATION;
    }

    @RequestMapping(value = PATH_RESEND_VER_CODE, method = RequestMethod.POST)
    public String resendVerificationCode(HttpServletRequest request, Model uiModel) {
        String pesel = request.getParameter("pesel");
        String email = request.getParameter("email");
        LOGGER.info("resendVerificationCode, PESEL={}, email={}",pesel, email);

        VerificationDto verificationDto = new VerificationDto(pesel, email);
        String comebackPage;
        try {
            verificationService.resendVerificationCode(verificationDto);
            comebackPage = PAGE_VER_CODE_SUCCESS;
        } catch (GryfVerificationException e){
            uiModel.addAttribute("error", e);
            comebackPage = PAGE_VERIFICATION;
        } catch(GryfRuntimeException e) {
            uiModel.addAttribute("unknowerror", e);
            comebackPage = PAGE_VERIFICATION;
        }
        return comebackPage;
    }
}
