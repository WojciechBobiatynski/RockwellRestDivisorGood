package pl.sodexo.it.gryf.web;


/**
 * Keeps constants for resolving views.
 * 
 * @author Michal.CHWEDCZUK.ext
 */
public class ViewResolverAttributes {
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
