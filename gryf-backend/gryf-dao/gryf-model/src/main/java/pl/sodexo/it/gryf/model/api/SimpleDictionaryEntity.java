package pl.sodexo.it.gryf.model.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.math.BigInteger;

/**
 * Encja reprezentuająca kategorię szkolenia
 *
 * Created by kantczak on 2016-10-26.
 */
@ToString
@MappedSuperclass
public abstract class SimpleDictionaryEntity extends AuditableEntity {

    @Column(name = "ORDINAL", nullable = false)
    @Getter
    @Setter
    private BigInteger ordinal;

    @Column(name = "DESCRIPTION", nullable = false)
    @Getter
    @Setter
    private String description;
}
