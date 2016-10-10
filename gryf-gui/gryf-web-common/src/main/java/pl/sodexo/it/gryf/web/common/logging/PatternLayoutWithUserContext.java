package pl.sodexo.it.gryf.web.common.logging;

import ch.qos.logback.classic.PatternLayout;

/**
 * Klasa rozszerzajaca layout logbacka o informacje na temat loginu oraz roli uzytkownika.
 *
 * Created by adziobek on 28.09.2016.
 */
public class PatternLayoutWithUserContext extends PatternLayout {

    static {
        PatternLayout.defaultConverterMap.put("user", UserConverter.class.getName());
        PatternLayout.defaultConverterMap.put("role", UserRoleConverter.class.getName());
    }
}
