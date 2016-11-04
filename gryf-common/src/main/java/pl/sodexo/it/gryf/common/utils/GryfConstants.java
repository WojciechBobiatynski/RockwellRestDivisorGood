package pl.sodexo.it.gryf.common.utils;

/**
 * Klasa pomocnicza przechowująca stałe aplikacji
 *
 * Created by akmiecinski on 21.10.2016.
 */
public final class GryfConstants {

    private GryfConstants(){};

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String VER_CODE_CHARS = "qwertyuipasdfghjklzxcvbnmQWERTYUIPASDFGHJKLZXCVBNM123456789";

    public static final String VERIFICATION_CODE_EMAIL_TEMPLATE_CODE = "VC_SEND";
    public static final String RESET_LINK_EMAIL_TEMPLATE_CODE = "RESET_LINK";

    public static final String EMAIL_BODY_VER_CODE_PLACEHOLDER = "verificationCode";
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
}
