package com.account.transfer.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

/**
 * Created by shridhar on 23/02/19.
 */
public class AccountException extends WebApplicationException {


    private static final long serialVersionUID = -1704032562249511584L;

    public AccountException() {
        super();
    }

    public AccountException(String message) {
        super(Response.status(BAD_REQUEST)
                .entity(new ErrorResponse(BAD_REQUEST.getStatusCode(), message))
                .type(MediaType.APPLICATION_JSON)
                .build());
    }

}
