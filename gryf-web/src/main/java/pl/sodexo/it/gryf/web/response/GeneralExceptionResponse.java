/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.web.response;

public class GeneralExceptionResponse {

    private final ResponseType responseType = ResponseType.GENERAL_EXCEPTION;
    private final String message;

    public ResponseType getResponseType() {
        return responseType;
    }

    public GeneralExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    
    
    
}
