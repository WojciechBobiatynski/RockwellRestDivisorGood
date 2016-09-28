package pl.sodexo.it.gryf.service.utils;

import org.springframework.context.ApplicationContext;
import pl.sodexo.it.gryf.service.local.api.publicbenefits.grantapplications.GrantApplicationService;

/**
 * Created by jbentyn on 2016-09-28.
 */
public class GrantApplicationBeanUtils {

    public static GrantApplicationService findGrantApplicationService(ApplicationContext context, String serviceBeanName) {
        return (GrantApplicationService) BeanUtils.findBean(context, serviceBeanName);
    }
}
