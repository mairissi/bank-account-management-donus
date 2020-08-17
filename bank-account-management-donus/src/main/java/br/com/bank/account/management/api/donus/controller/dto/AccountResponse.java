package br.com.bank.account.management.api.donus.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;

@Getter
@Setter
public class AccountResponse {

    private Long id;
    private String bank;
    private String accountNumber;
    private BigDecimal balance;
    private OffsetDateTime dateOpened;
    private OffsetDateTime dataClosed;
    private String active;
    private String customerCpf;
    private String customerFullName;

    public void setBalance(BigDecimal balance){
        this.balance = balance.setScale(2, RoundingMode.HALF_UP);
    }
}