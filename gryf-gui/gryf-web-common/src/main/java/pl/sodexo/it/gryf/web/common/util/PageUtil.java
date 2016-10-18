package pl.sodexo.it.gryf.web.common.util;

import org.springframework.ui.Model;

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

}
