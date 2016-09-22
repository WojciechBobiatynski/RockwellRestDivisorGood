package pl.sodexo.it.gryf.root.service.impl;

import org.springframework.context.ApplicationContext;

/**
 * Created by akuchna on 2016-09-21.
 */
public final class BeanUtils {

    private BeanUtils() {}

    public static Object findBean(ApplicationContext context, String beanName) {
        Object service = context.getBean(beanName);
        if (service == null) {
            throw new RuntimeException("Nie udało się pobrać bean springowego o nazwie " + beanName);
        }
        return service;
    }
}
