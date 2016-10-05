/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.web.response;

import pl.sodexo.it.gryf.common.enums.Privileges;

public class UnauthorizedResponse {

    private final static ResponseType responseType = ResponseType.UNAUTHORIZED;
    private final Privileges[] requiredRole;
    public ResponseType getResponseType() {
        return responseType;
    }

    public UnauthorizedResponse(Privileges ... requiredRole) {
        this.requiredRole = requiredRole;
    }

    public Privileges[] getRequiredRole() {
        return requiredRole;
    }
    
}
