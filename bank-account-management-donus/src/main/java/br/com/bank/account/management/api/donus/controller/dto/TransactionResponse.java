package br.com.bank.account.management.api.donus.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;

@Getter
@Setter
public class TransactionResponse {

    private Long id;
    private String accountBankCode;
    private String accountNumber;
    private BigDecimal amount;
    private OffsetDateTime dateTransaction;
    private String typeOperation;
    private BigDecimal balance;

    public void setAmount(BigDecimal amount){
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public void setBalance(BigDecimal balance){
        this.balance = balance.setScale(2, RoundingMode.HALF_UP);
    }

}