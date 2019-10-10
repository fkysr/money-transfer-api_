package model;

import java.math.BigDecimal;
import java.util.Date;

public class Transfer {
    private Long transferId;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private Currency currency;

    public Long getTransferId() {
        return transferId;
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Transfer(long fromAccountId, long toAccountId, BigDecimal amount, Currency currency){
        this.fromAccountId= fromAccountId;
        this.toAccountId= toAccountId;
        this.amount= amount;
        this.currency= currency;
    }
}
