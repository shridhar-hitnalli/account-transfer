package com.account.transfer.services;

import com.account.transfer.exception.AccountAlreadyExistsException;
import com.account.transfer.exception.AccountException;
import com.account.transfer.exception.AccountNotFoundException;
import com.account.transfer.exception.InsufficientBalanceException;
import com.account.transfer.model.Account;
import com.account.transfer.model.Transfer;

import javax.inject.Singleton;
import javax.ws.rs.ext.Provider;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Created by shridhar on 23/02/19.
 */
@Provider
@Singleton
public class AccountService {

    private final Map<Integer, Account> inMemoryAccounts = new ConcurrentHashMap<>();
    private static final Map<Integer, ReentrantReadWriteLock> locks = new ConcurrentHashMap<>();

    public List<Account> getAllAccounts() {
        return inMemoryAccounts.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public Account getAccount(Integer accountId) {
        final Account account = inMemoryAccounts.get(accountId);
        if (account == null) {
            throw new AccountNotFoundException(String.format("Account with account number '%s' is not found", accountId));
        }
        return account;
    }

    public void openAccount(Account account) {

        Account existingAccount = inMemoryAccounts.putIfAbsent(account.getId(), account);
        if (existingAccount != null) {
            throw new AccountAlreadyExistsException(String.format("Account with account number '%s' already exists", account.getId()));
        }

    }

    public void transfer(Transfer transfer) {
        final Integer fromAccount = transfer.getFromAccount();
        final Integer toAccount = transfer.getToAccount();
        final BigDecimal amount = transfer.getAmount();

        if (Objects.equals(fromAccount,toAccount)) {
            throw new AccountException(String.format("Can not transfer money to the same account '%s' ", fromAccount));
        }

        Account accountFrom = getAccount(fromAccount);
        Account accountTo = getAccount(toAccount);

        final Lock firstLock = locks.computeIfAbsent(Math.min(fromAccount, toAccount), integer -> new ReentrantReadWriteLock()).writeLock();
        final Lock secondLock = locks.computeIfAbsent(Math.max(fromAccount, toAccount), integer -> new ReentrantReadWriteLock()).writeLock();

        firstLock.lock();
        secondLock.lock();

        try {

            if (accountFrom.getAmount().compareTo(amount) < 0) {
                throw new InsufficientBalanceException(String.format("Account with account number '%s' has insufficient balance ", accountFrom));
            }

            accountFrom.withdraw(amount);
            accountTo.deposit(amount);

        } finally {
            firstLock.unlock();
            secondLock.unlock();
        }

    }

    public void closeAccount(Integer accountId) {
        inMemoryAccounts.remove(accountId);
    }


    public Account deposit(Integer accountId, BigDecimal amount) {
        if (inMemoryAccounts.get(accountId) == null) {
            throw new AccountNotFoundException(String.format("Account with account number '%s' is not found", accountId));
        }
        inMemoryAccounts.computeIfPresent(accountId, (key, account) -> {
            account.deposit(amount);
            return account;
        });

        return inMemoryAccounts.get(accountId);

    }

    public Account withdrawal(Integer accountId, BigDecimal amount) {
        if (inMemoryAccounts.get(accountId) == null) {
            throw new AccountNotFoundException(String.format("Account with account number '%s' is not found", accountId));
        }
        inMemoryAccounts.computeIfPresent(accountId, (key, account) -> {
            if (account.getAmount().compareTo(amount) < 0) {
                throw new InsufficientBalanceException(String.format("Account with account number '%s' has insufficient balance ", accountId));
            }
            account.withdraw(amount);
            return account;
        });

        return inMemoryAccounts.get(accountId);

    }

}
