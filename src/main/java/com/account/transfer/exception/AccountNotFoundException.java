package com.account.transfer.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * Created by shridhar on 23/02/19.
 */
@Provider
public class AccountNotFoundException extends WebApplicationException {


    private static final long serialVersionUID = -9025806348148467724L;

    public AccountNotFoundException() {
        super();
    }

    public AccountNotFoundException(String message) {
        super(Response.status(NOT_FOUND)
                .entity(new ErrorResponse(NOT_FOUND.getStatusCode(), message))
                .type(MediaType.APPLICATION_JSON)
                .build());
    }


}
