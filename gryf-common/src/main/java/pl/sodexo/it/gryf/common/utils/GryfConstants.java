package pl.sodexo.it.gryf.common.utils;

/**
 * Klasa pomocnicza przechowująca stałe aplikacji
 *
 * Created by akmiecinski on 21.10.2016.
 */
public final class GryfConstants {

    private GryfConstants(){};

    public static final String FLAG_TRUE = "Y";
    public static final String FLAG_FALSE = "N";

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String CHAR_SET_UTF_8_ENCODING = "UTF-8";

    public static final String VERIFICATION_CODE_EMAIL_TEMPLATE_CODE = "VC_SEND";
    public static final String RESET_LINK_EMAIL_TEMPLATE_CODE = "RESET_LINK";
    public static final String SEND_TRAINING_REIMBURSMENT_PIN_TEMPLATE_CODE = "PIN_SEND";
    public static final String RESEND_TRAINING_REIMBURSMENT_PIN_TEMPLATE_CODE = "PIN_RESEND";
    public static final String REALIZE_ORDER_TEMPLATE_CODE = "KK_ORDER";


    public static final String EMAIL_BODY_VER_CODE_PLACEHOLDER = "verificationCode";
    public static final String EMAIL_BODY_LOGIN_PLACEHOLDER = "login";
    public static final String EMAIL_BODY_URL_PLACEHOLDER = "url";
    public static final String EMAIL_BODY_RESET_LINK_PLACEHOLDER = "resetLink";

    public static final String DEFAULT_FAILURE_LOGIN_URL = "/login?error";
    public static final String RESET_LINK_URL_PREFIX = "/reset/";

    public static final int DEFAULT_LOGIN_FAILURE_ATTEMPTS_NUMBER = 0;
    public static final int DEFAULT_RESET_FAILURE_ATTEMPTS_NUMBER = 0;

    public static final String JSP_ERROR_PLACEHOLDER = "error";
    public static final String JSP_UNKNOWNERROR_PLACEHOLDER = "unknownerror";
    public static final String JSP_SITE_KEY_PLACEHOLDER = "siteKey";
    public static final String JSP_PESEL_PLACEHOLDER = "pesel";
    public static final String JSP_EMAIL_PLACEHOLDER = "email";
    public static final String JSP_G_RECAPTCHA_RESPONSE_PLACEHOLDER = "g-recaptcha-response";
    public static final String JSP_CAPTCHA_SECRET_PARAM_PLACEHOLDER = "secret";
    public static final String JSP_CAPTCHA_RESPONSE_PARAM_PLACEHOLDER = "response";
    public static final String JSP_LOGIN_PLACEHOLDER = "login";
    public static final String JSP_PASSWORD_PLACEHOLDER = "password";
    public static final String JSP_REPEATED_PASSWORD_PLACEHOLDER = "repeatedPassword";

    public static final int FIRST_PASSWORD_DEFAULT_LENGTH_FOR_TI = 10;

    public static final String NEW_ERMBS_STATUS_CODE = "NEW";
    public static final String TO_REIMBURSE_ERMBS_STATUS_CODE = "T_RMS";

    public static final String TO_REIMBURSE_TRAINING_INSTANCE_STATUS_CODE = "T_RMS";
}
