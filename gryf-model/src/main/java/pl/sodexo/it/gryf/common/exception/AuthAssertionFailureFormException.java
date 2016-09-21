package pl.sodexo.it.gryf.common.exception;

import pl.sodexo.it.gryf.common.Privileges;

public class AuthAssertionFailureFormException extends RuntimeException {
    private final Privileges[] privilegesRequired;

    public AuthAssertionFailureFormException(Privileges ... privileges) {
        this.privilegesRequired = privileges;
    }

    public Privileges[] getPrivilegesRequired() {
        return privilegesRequired;
    }
    
}
