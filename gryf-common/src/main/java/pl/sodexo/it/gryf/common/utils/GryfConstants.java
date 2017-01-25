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
    public static final String TI_ACCESS_EMAIL_TEMPLATE_CODE = "TI_ACCESS";
    public static final String REALIZE_ORDER_TEMPLATE_CODE = "KK_ORDER";
    public static final String CORR_NOTIF_EMAIL_TEMPLATE_CODE = "CORR_NOTIF";
    public static final String E_REIMB_CONFIRMATION_EMAIL_TEMPLATE_CODE = "E_REIMB";
    public static final String E_REIMB_CONFIRMATION_EMAIL_TEMPLATE_CODE_FOR_UNRSV_POOL = "RET_REIMB";
    public static final String EXPI_REIMB_EMAIL_TEMPLATE_CODE = "EXPI_REIMB";
    public static final String CONFIRMATION_PAYMENT_EMAIL_TEMPLATE_CODE = "CNF_PYMT";
    public static final String CONFIRMATION_PAYMENT_EMAIL_TEMPLATE_CODE_FOR_ENTERPRISE = "E_CONF_GRA";
    public static final String DEFAULT_EMAIL_TEMPLATE_CODE = "STD_EMAIL";

    public static final String GRANT_PROGRAM_PLACEHOLDER = "grantProgramName";
    public static final String ARRIVAL_DATE_PLACEHOLDER = "arrivalDate";
    public static final String RMBS_NUMBER_PLACEHOLDER = "rmbsNumber";
    public static final String NOTE_NOT_PLACEHOLDER = "noteNo";

    public static final String FIRST_NAME_PLACEHOLDER = "firstName";
    public static final String LAST_NAME_PLACEHOLDER = "lastName";
    public static final String TRAINING_NAME_PLACEHOLDER = "trainingName";

    public static final String EMAIL_BODY_VER_CODE_PLACEHOLDER = "verificationCode";
    public static final String EMAIL_BODY_LOGIN_PLACEHOLDER = "login";
    public static final String EMAIL_BODY_URL_PLACEHOLDER = "url";
    public static final String EMAIL_BODY_RESET_LINK_PLACEHOLDER = "resetLink";

    public static final String EMAIL_BODY_PLACEHOLDER = "emailBody";
    public static final String EMAIL_SUBJECT_PLACEHOLDER = "emailSubject";

    public static final String DEFAULT_FAILURE_LOGIN_URL = "/login?error";
    public static final String RESET_LINK_URL_PREFIX = "reset/";

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

    public static final String TO_REIMBURSE_TRAINING_INSTANCE_STATUS_CODE = "T_RMS";
    public static final String DONE_TRAINING_INSTANCE_STATUS_CODE = "DONE";
    public static final String REIMBURSED_TRAINING_INSTANCE_STATUS_CODE = "REIMB";
    public static final String NOT_REIMBURSED_TRAINING_INSTANCE_STATUS_CODE = "NOT_REIMB";

    public static final String ATT_CORR_SUFFIX = "_CORR";
    public static final String FILE_EXTENSION_DELIMITER = ".";

    public static final Integer BIG_DECIMAL_MONEY_SCALE = 2;
    public static final Integer BIG_DECIMAL_INTEGER_SCALE = 0;

    public static final String BUSINESS_DAYS_FOR_REIMBURSEMENT_PARAM_NAME = "D_FOR_REIM";
    public static final String BUSINESS_DAYS_FOR_CORRECTION_PARAM_NAME = "D_FOR_CORR";
}
