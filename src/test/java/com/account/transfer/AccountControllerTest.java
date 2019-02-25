package com.account.transfer;

import com.account.transfer.config.AccountApplicationBinder;
import com.account.transfer.controller.AccountController;
import com.account.transfer.model.Account;
import com.account.transfer.model.Transfer;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * Created by shridhar on 24/02/19.
 */
public class AccountControllerTest extends JerseyTest {

    @Override
    public javax.ws.rs.core.Application configure() {
        return new ResourceConfig(AccountController.class)
                .register(new AccountApplicationBinder());

    }


    @Test
    public void test_success_open_account() {
        test_post_success(1, "200");
    }

    @Test
    public void test_error_already_existed_account() {
        //cant open already existed account
        assertEquals(200, createAccountAndGetResponse(1, new BigDecimal("100")));
        assertEquals(400, createAccountAndGetResponse(1, new BigDecimal("200")));
        Account actual = getAccountAndCheckStatusOk(1);
        assertEquals(new Account(1, BigDecimal.valueOf(100L)), actual);
    }

    @Test
    public void test_success_transfer() {
        testTransfer(1, 2, "10", 200, "90", "110");
        //remove accounts again to restart
        removeAccount(1);
        removeAccount(2);
        testTransfer(2, 1, "100", 200, "0", "200");
        removeAccount(1);
        removeAccount(2);
        testTransfer(1, 2, "1.1", 200, "98.9", "101.1");
        removeAccount(1);
        removeAccount(2);
        testTransfer(1, 2, "0.0000000001", 200, "99.9999999999", "100.0000000001");
    }

    @Test
    public void test_error_transfer_not_found() {
        //account not found
        testTransfer(2, 3, "50", 404, "100", "");
    }

    @Test
    public void test_error_transfer_invalid_input() {
        //negative values not allowed
        testTransfer(1, 2, "-101", 400, "100", "100");
    }

    @Test
    public void test_error_transfer_same_accounts() {
        //same account transfer not allowed
        testTransfer(1, 1, "100", 400, "100", "100");
    }

    @Test
    public void test_error_transfer_insufficient_balance() {
        //insufficient balance error
        testTransfer(1, 2, "200", 400, "", "100");
    }

    @Test
    public void test_success_deposit() {
        assertEquals(200, createAccountAndGetResponse(1, new BigDecimal("100")));
        Response response = deposit(1, new BigDecimal("200"));
        assertEquals(200, response.getStatus());
        assertAccount(1, "300");
    }

    @Test
    public void test_error_deposit() {
        Response response = deposit(1, new BigDecimal("200"));
        assertEquals(404, response.getStatus());
    }

    @Test
    public void test_success_withdraw() {
        assertEquals(200, createAccountAndGetResponse(1, new BigDecimal("100")));
        Response response = withdraw(1, new BigDecimal("10"));
        assertEquals(200, response.getStatus());
        assertAccount(1, "90");
    }

    @Test
    public void test_withdraw_error_not_found() {
        Response response = withdraw(1, new BigDecimal("200"));
        assertEquals(404, response.getStatus());
    }

    @Test
    public void test_withdraw_error_insufficient_balance() {
        assertEquals(200, createAccountAndGetResponse(1, new BigDecimal("100")));
        Response response = withdraw(1, new BigDecimal("200"));
        assertEquals(400, response.getStatus());
    }

    private Response deposit(Integer id, BigDecimal amount) {
        return target(String.format("/accounts/%d/deposit", id)).request().put(Entity.json("{\"id\":" + id + ",\"amount\":" + amount + "}"));
    }

    private Response withdraw(Integer id, BigDecimal amount) {
        return target(String.format("/accounts/%d/withdraw", id)).request().put(Entity.json("{\"id\":" + id + ",\"amount\":" + amount + "}"));
    }

    private void test_post_success(Integer id, String amount) {
        int actualPostResponseCode = createAccountAndGetResponse(id, new BigDecimal(amount));
        assertEquals(200, actualPostResponseCode);
        Account actual = getAccountAndCheckStatusOk(id);
        assertEquals(new Account(id, new BigDecimal(amount)), actual);

    }


    private void testTransfer(Integer accountNumberFrom, Integer accountNumberTo, String amount,
                      int expectedResponseCode,
                      String expectedAccountFromAmount, String expectedAccountToAmount) {
        Transfer transfer = new Transfer(accountNumberFrom, accountNumberTo, new BigDecimal(amount));

        assertTransfer(accountNumberFrom, accountNumberTo,
                expectedResponseCode, expectedAccountFromAmount, expectedAccountToAmount,transfer);
    }


    private void assertTransfer(Integer accountNumberFrom, Integer accountNumberTo,
                                int expectedResponseCode,
                                String expectedAccountFromAmount, String expectedAccountToAmount,
                                Transfer transfer) {

        assertEquals(200, createAccountAndGetResponse(1, new BigDecimal("100")));
        assertEquals(200, createAccountAndGetResponse(2, new BigDecimal("100")));

        Response response = target("/accounts/transfer")
                .request()
                .post(Entity.json("{\"fromAccount\":" + transfer.getFromAccount() + ",\"toAccount\":" + transfer.getToAccount() + ",\"amount\":" + transfer.getAmount() + "}"));

        assertEquals(expectedResponseCode, response.getStatus());

        assertAccount(accountNumberFrom, expectedAccountFromAmount);
        assertAccount(accountNumberTo, expectedAccountToAmount);
    }

    private void assertAccount(Integer accountNumber, String expectedAmount) {
        if (expectedAmount.equals("")) {
            return;
        }

        Account account = getAccountAndCheckStatusOk(accountNumber);
        assertEquals(new Account(accountNumber, new BigDecimal(expectedAmount)), account);
    }

    private int createAccountAndGetResponse(Integer id, BigDecimal amount) {
        return target("/accounts")
                .request()
                .post(Entity.json("{\"id\":" + id + ",\"amount\":" + amount + "}"))
                .getStatus();
    }

    private Account getAccountAndCheckStatusOk(Integer id) {
        Response response = target(String.format("/accounts/%d", id))
                .request().get();

        assertEquals(Response.Status.OK.toString(), response.getStatusInfo().toString());
        return response.readEntity(Account.class);
    }


    private void removeAccount(Integer id) {
        int status = target(String.format("/accounts/%d", id)).request().delete().getStatus();
        assertEquals(200, status);
    }
}

