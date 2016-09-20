package pl.sodexo.it.gryf.exception;

import pl.sodexo.it.gryf.Privileges;

public class AuthAssertionFailureException extends RuntimeException {
    private final Privileges[] privilegesRequired;

    public AuthAssertionFailureException(Privileges ... privileges) {
        this.privilegesRequired = privileges;
    }

    public Privileges[] getPrivilegesRequired() {
        return privilegesRequired;
    }
    
}
