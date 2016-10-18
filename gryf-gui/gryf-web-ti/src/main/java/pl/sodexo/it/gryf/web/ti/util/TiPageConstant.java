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
}
