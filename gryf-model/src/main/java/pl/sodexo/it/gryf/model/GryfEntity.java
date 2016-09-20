package pl.sodexo.it.gryf.model;

import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import java.io.Serializable;

/**
 * Created by tomasz.bilski.ext on 2015-06-17.
 */
@MappedSuperclass
@SequenceGenerator(name="pk_seq", schema = "eagle", sequenceName = "pk_seq", allocationSize = 1)
public class GryfEntity implements Serializable {
}
