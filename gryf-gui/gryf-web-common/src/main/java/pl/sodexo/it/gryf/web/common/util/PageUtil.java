package pl.sodexo.it.gryf.web.common.util;

import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

/**
 * Funkcje pomocnicze dotyczace stron.
 * 
 * Created by akuchna on 2016-10-10.
 */
public final class PageUtil {

    private PageUtil() {}

    private static final String PARAM_MAIN_CONTENT = "pageMainContent";

    public static void setUpMainContent(Model model, String subPage){
        model.addAttribute(PARAM_MAIN_CONTENT, subPage);
    }

    public static String getURLWithContextPath(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
