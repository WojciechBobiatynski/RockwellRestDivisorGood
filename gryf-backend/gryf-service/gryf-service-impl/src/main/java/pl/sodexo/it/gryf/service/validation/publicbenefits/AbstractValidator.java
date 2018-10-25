package pl.sodexo.it.gryf.service.validation.publicbenefits;

import org.apache.commons.collections.CollectionUtils;
import pl.sodexo.it.gryf.common.exception.EntityConstraintViolation;

import java.util.List;

public abstract class AbstractValidator {

    /**
     * Dodaje prefix do wiadomości błedów walidacyjnych obiektów encyjnych JPA (DAO)
     * (javax.validation)
     *
     * @param prefix
     * @param violations
     */
    public final void addPrefixMessage(String prefix, List<EntityConstraintViolation> violations) {
        if (CollectionUtils.isNotEmpty(violations)) {
            violations.stream().forEach(violation -> {
                addPrefixMessage(prefix, violation);
            });
        }
    }

    private final void addPrefixMessage(String prefix, EntityConstraintViolation violation) {
        violation.setMessage(prefix + violation.getMessage());
    }
}
