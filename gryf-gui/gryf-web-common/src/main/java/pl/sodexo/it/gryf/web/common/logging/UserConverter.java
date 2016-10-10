package pl.sodexo.it.gryf.web.common.logging;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;

/**
 * Konwerter zdarzen loggera dodajacy informacje na temat zalogowanego uzytkownika.
 *
 * Created by adziobek on 28.09.2016.
 */
public class UserConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        GryfUser user = GryfUser.getLoggedUser();
        if (user == null) {
            return "NO_USER";
        }
        return user.getUser().getLogin();
    }

}
