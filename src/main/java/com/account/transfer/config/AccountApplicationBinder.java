package com.account.transfer.config;

import com.account.transfer.services.AccountService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.ws.rs.ext.Provider;

/**
 * Created by shridhar on 23/02/19.
 */
@Provider
public class AccountApplicationBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(AccountService.class).to(AccountService.class);
    }
}
