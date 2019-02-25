package com.account.transfer.model;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by shridhar on 22/02/19.
 */
public class Transfer {

    @NotNull
    private Integer fromAccount;

    @NotNull
    private Integer toAccount;

    @NotNull
    @DecimalMin(value = "0", inclusive = false)
    private BigDecimal amount;

    public Transfer() {

    }

    public Transfer(Integer fromAccount, Integer toAccount, BigDecimal amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    public Integer getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Integer fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Integer getToAccount() {
        return toAccount;
    }

    public void setToAccount(Integer toAccount) {
        this.toAccount = toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transfer transfer = (Transfer) o;

        if (fromAccount != null ? !fromAccount.equals(transfer.fromAccount) : transfer.fromAccount != null)
            return false;
        if (toAccount != null ? !toAccount.equals(transfer.toAccount) : transfer.toAccount != null) return false;
        return amount != null ? amount.equals(transfer.amount) : transfer.amount == null;

    }

    @Override
    public int hashCode() {
        int result = fromAccount != null ? fromAccount.hashCode() : 0;
        result = 31 * result + (toAccount != null ? toAccount.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "fromAccount=" + fromAccount +
                ", toAccount=" + toAccount +
                ", amount=" + amount +
                '}';
    }
}
