package pl.sodexo.it.gryf.service.utils;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sodexo.it.gryf.common.config.ApplicationParameters;
import pl.sodexo.it.gryf.service.api.utils.GryfAccessCodeGenerator;

/**
 * Created by adziobek on 21.11.2016.
 */
@Component
public class GryfAccessCodeGeneratorImpl implements GryfAccessCodeGenerator {

    public static final String VER_CODE_CHARS = "qwertyuipasdfghjkzxcvbnmQWERTYUPASDFGHJKZXCVBNM123456789";
    public static final String PIN_CHARS = "qwertyuipasdfghjkzxcvbnmQWERTYUPASDFGHJKZXCVBNM23456789";

    @Autowired
    private ApplicationParameters applicationParameters;

    @Override
    public String createVerificationCode() {
        return RandomStringUtils.random(applicationParameters.getVerificationCodeLength(), VER_CODE_CHARS);
    }

    @Override
    public String createReimbursmentPin() {
        return RandomStringUtils.random(applicationParameters.getVerificationCodeLength(), PIN_CHARS);
    }
}