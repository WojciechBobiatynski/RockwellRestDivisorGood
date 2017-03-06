package pl.sodexo.it.gryf.web.ti.util;

import pl.sodexo.it.gryf.web.common.util.PageConstant;

/**
 * Stale dotyczace stron dla TI.
 * 
 * Created by akuchna on 2016-10-10.
 */
public final class TiPageConstant extends PageConstant {

    private TiPageConstant() {}

    private static final String PREFIX_PAGE = "/WEB-INF/page/";

    public static final String PATH_TI_LOGIN = "/login";
    public static final String PAGE_TI_LOGIN = PREFIX_PAGE + "ti-login.jsp";
    
    public static final String SUB_PAGE_TI_WELCOME = PREFIX_PAGE + "ti-welcome.jsp";

    public static final String PATH_RETRIEVE_PASSWORD = "/retrieve";
    public static final String PAGE_RETRIEVE_PASSWORD = PREFIX_PAGE + "password/retrieve/retrievePassword.jsp";

    public static final String PATH_RETRIEVE_RESET_PASSWORD = "/retrieve/password";
    public static final String PAGE_RETRIEVE_RESET_PASSWORD_SUCCESS = PREFIX_PAGE + "password/retrieve/retrievePasswordSuccess.jsp";

    public static final String PATH_RESET_PASSWORD = "/reset";
    public static final String PAGE_RESET_PASSWORD = PREFIX_PAGE + "password/save/savePassword.jsp";

    public static final String PAGE_RESET_PASSWORD_EXCEPTION = PREFIX_PAGE + "password/resetException.jsp";

    public static final String PATH_SAVE_PASSWORD = "/password/save";
    public static final String PAGE_SAVE_SUCCESS = PREFIX_PAGE + "password/save/savePasswordSuccess.jsp";

    public static final String PATH_HELP = "/help";
    public static final String PAGE_HELP = PREFIX_PAGE + "help/help.jsp";
    public static final String PAGE_ERROR_CODE = PREFIX_PAGE + "error/error.jsp?code=%s";
    public static final String PAGE_ERROR = PREFIX_PAGE + "error/error.jsp";
}
