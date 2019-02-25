package com.account.transfer.controller;

import com.account.transfer.model.Account;
import com.account.transfer.model.Transfer;
import com.account.transfer.services.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;


/**
 * Created by shridhar on 22/02/19.
 */
@Path("accounts")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;

    @Inject
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response openAccount(@Valid Account account) {
        logger.debug("AccountController : in openAccount method ");
        accountService.openAccount(account);
        return Response.ok().build();
    }

    @GET
    public List<Account> getAllAccounts() throws Exception {
        logger.debug("AccountController : in getAllAccounts method ");
        return accountService.getAllAccounts();
    }

    @GET
    @Path("/{id}")
    public Account getAccount(@PathParam("id") @NotNull Integer accountId) {
        logger.debug("AccountController : in getAccount method ");
        return accountService.getAccount(accountId);
    }

    @PUT
    @Path("/{id}/deposit")
    @Consumes(MediaType.APPLICATION_JSON)
    public Account deposit(@PathParam("id") @NotNull Integer accountId, @Valid Account account) {
        logger.debug("AccountController : in deposit method ");
        return accountService.deposit(accountId, account.getAmount());
    }

    @PUT
    @Path("/{id}/withdraw")
    @Consumes(MediaType.APPLICATION_JSON)
    public Account withdrawal(@PathParam("id") @NotNull Integer accountId, @Valid Account account) {
        logger.debug("AccountController : in withdrawal method ");
        return accountService.withdrawal(accountId, account.getAmount());
    }

    @POST
    @Path("/transfer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transfer(@Valid Transfer transfer)throws Exception  {
        logger.debug("AccountController : in transfer method ");
        accountService.transfer(transfer);
        return Response.ok().build();
    }

    @GET
    @Path("/{id}/balance")
    public BigDecimal getBalance(@PathParam("id") @NotNull Integer accountId) {
        logger.debug("AccountController : in getBalance method ");

        final Account account = accountService.getAccount(accountId);

        return account.getAmount();
    }

    @DELETE
    @Path("/{id}")
    public Response closeAccount(@PathParam("id") @NotNull Integer accountId) {
        logger.debug("AccountController : in closeAccount method ");

        accountService.closeAccount(accountId);
        return Response.ok().build();
    }



}
