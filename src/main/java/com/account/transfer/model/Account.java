package com.account.transfer.model;


import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by shridhar on 22/02/19.
 */

public class Account {

    @NotNull
    private Integer id;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal amount;

    public Account() {

    }

    public Account(Integer id, BigDecimal amount) {
        this.id = id;
        this.amount = amount;
    }


    public Integer getId() {
        return id;
    }


    public BigDecimal getAmount() {
        return amount;
    }


    public void withdraw(BigDecimal amount) {
        this.amount = this.amount.subtract(amount);
    }

    public void deposit(BigDecimal amount) {
        this.amount = this.amount.add(amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (id != null ? !id.equals(account.id) : account.id != null)
            return false;
        return amount != null ? amount.equals(account.amount) : account.amount == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber=" + id +
                ", amount=" + amount +
                '}';
    }
}
