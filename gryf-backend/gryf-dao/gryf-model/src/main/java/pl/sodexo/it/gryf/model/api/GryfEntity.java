package pl.sodexo.it.gryf.model.api;

import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.user.GryfUser;

import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tomasz.bilski.ext on 2015-06-17.
 */
@ToString
@MappedSuperclass
@SequenceGenerator(name="pk_seq", schema = "eagle", sequenceName = "pk_seq", allocationSize = 1)
public class GryfEntity implements Serializable {

    protected static String getEagleAuditableInfo(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdf.format(date) + " " +
                (!GryfUser.getLoggedUserLogin().isEmpty() ? GryfUser.getLoggedUserLogin() : "GRYF" );
    }
}
