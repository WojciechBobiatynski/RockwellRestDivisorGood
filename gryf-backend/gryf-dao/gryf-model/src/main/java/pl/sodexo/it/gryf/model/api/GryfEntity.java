package pl.sodexo.it.gryf.model.api;

import lombok.ToString;

import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import java.io.Serializable;

/**
 * Created by tomasz.bilski.ext on 2015-06-17.
 */
@ToString
@MappedSuperclass
@SequenceGenerator(name="pk_seq", schema = "eagle", sequenceName = "pk_seq", allocationSize = 1)
public class GryfEntity implements Serializable {
}
