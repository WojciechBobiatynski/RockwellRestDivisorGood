package pl.sodexo.it.gryf.common.utils;

/**
 * Klasa pomocnicza przechowująca stałe aplikacji
 *
 * Created by akmiecinski on 21.10.2016.
 */
public final class GryfConstants {

    private GryfConstants(){};

    public static final String VERIFICATION_CODE_EMAIL_TEMPLATE_CODE = "VC_SEND";
    public static final String RESET_LINK_EMAIL_TEMPLATE_CODE = "RESET_LINK";
    public static final String EMAIL_BODY_VER_CODE_PLACEHOLDER = "verificationCode";
    public static final String EMAIL_BODY_RESET_LINK_PLACEHOLDER = "resetLink";
    public static final String DEFAULT_FAILURE_LOGIN_URL = "/login?error";
    public static final int DEFAULT_LOGIN_FAILURE_ATTEMPTS_NUMBER = 0;
    public static final int DEFAULT_RESET_FAILURE_ATTEMPTS_NUMBER = 0;
    public static final String RESET_LINK_URL_PREFIX = "/reset/";
}
