package pl.sodexo.it.gryf.common.exception;

import pl.sodexo.it.gryf.common.Privileges;

public class AuthAssertionFailureException extends RuntimeException {
    private final Privileges[] privilegesRequired;

    public AuthAssertionFailureException(Privileges ... privileges) {
        this.privilegesRequired = privileges;
    }

    public Privileges[] getPrivilegesRequired() {
        return privilegesRequired;
    }
    
}
