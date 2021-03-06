package pl.sodexo.it.gryf.web.fo.utils;

import pl.sodexo.it.gryf.web.common.util.PageConstant;

/**
 * Stale dotyczace stron dla FO.
 * 
 * Created by akuchna on 2016-10-13.
 */
public final class FoPageConstant extends PageConstant {

    private FoPageConstant() {}

    private static final String PREFIX_PAGE = "/WEB-INF/pages/";

    public static final String PATH_FO_LOGIN = "/login";
    public static final String PAGE_FO_LOGIN = PREFIX_PAGE + "fo-login.jsp";

    public static final String SUB_PAGE_FO_WELCOME = PREFIX_PAGE + "fo-welcome.jsp";
    
    public static final String PATH_FO_ZIP = "/zipCodes/";
    public static final String SUB_PAGE_FO_ZIP = PREFIX_PAGE + "dictionaries/zipCodesIndex.jsp";

    public static final String VER_CODE_GENERATE_PATH = "/verification/generate";
    public static final String VER_CODE_SEND_PATH = "/verification/send";

    public static final String SEND_RESET_PASSWORD_LINK = "/reset";
}

