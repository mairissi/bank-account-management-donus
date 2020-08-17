package br.com.bank.account.management.api.donus.controller.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
public class TransactionRequest {

    @NotNull(message = "Amount can not be null")
    @DecimalMin(value = "0", inclusive = false, message = "Amount must be greater than 0")
    private BigDecimal amount;

    public void setAmount(BigDecimal amount){
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
    }
}
