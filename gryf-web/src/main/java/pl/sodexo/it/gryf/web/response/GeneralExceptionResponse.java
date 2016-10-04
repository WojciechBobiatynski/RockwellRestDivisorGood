/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.web.response;

public class GeneralExceptionResponse {

    private final ResponseType responseType = ResponseType.GENERAL_EXCEPTION;
    private final String message;
    private final String stacktrace;

    public ResponseType getResponseType() {
        return responseType;
    }

    public GeneralExceptionResponse(String message, String stacktrace) {

        this.message = message;
        this.stacktrace = stacktrace;
    }

    public String getMessage() {
        return message;
    }

    public String getStacktrace() { return stacktrace; }
}
