package pl.sodexo.it.gryf.web.ind.util;

import pl.sodexo.it.gryf.web.common.util.PageConstant;

/**
 * Stale dotyczace stron dla IND.
 * 
 * Created by akuchna on 2016-10-14.
 */
public final class IndPageConstant extends PageConstant {

    private IndPageConstant() {}

    private static final String PREFIX_PAGE = "/WEB-INF/page/";

    public static final String PATH_IND_LOGIN = "/login";
    public static final String PAGE_IND_LOGIN = PREFIX_PAGE + "ind-login.jsp";

    public static final String MAIN_PAGE = PREFIX_PAGE + "index.jsp";

    public static final String PATH_VERIFICATION = "/verification";
    public static final String PAGE_VERIFICATION = PREFIX_PAGE + "verification/remindVerificationCode.jsp";

    public static final String PATH_VERIFICATION_RESEND_CODE = "verification/resend";

    public static final String PAGE_VERIFICATION_RESEND_SUCCESS = PREFIX_PAGE + "verification/sendVerCodeSuccess.jsp";

    public static final String PAGE_REIMBURSMENT_PIN_SEND_SUCCESS = PREFIX_PAGE + "index.jsp";

}
