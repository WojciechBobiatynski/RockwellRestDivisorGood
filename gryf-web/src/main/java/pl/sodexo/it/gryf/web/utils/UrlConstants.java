package pl.sodexo.it.gryf.web.utils;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-21.
 */
public final class UrlConstants {

    private UrlConstants() {}

    public static final String PUBLIC_BENEFITS_REST = "/rest/publicBenefits";
    public static final String DICTIONARIES_REST = "/rest/dictionaries";

    public static final String PAGES_PREFIX = "/WEB-INF/pages/";

    /**
     * It's a path to a base file which should be used for all views.
     */
    public static final String DEFAULT_VIEW = "/WEB-INF/pages/index.jsp";

    /**
     * It's a path to a login page.
     */
    public static final String LOGIN_VIEW = "/WEB-INF/pages/login.jsp";

    /**
     * An attribute name which should be put in to the model in a controller
     * to store link for individual page content.
     */
    public static final String MAIN_CONTENT_PARAM_NAME = "pageMainContent";
}
