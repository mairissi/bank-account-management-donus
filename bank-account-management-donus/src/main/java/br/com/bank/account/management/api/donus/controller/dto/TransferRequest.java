package br.com.bank.account.management.api.donus.controller.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class TransferRequest {

    @NotNull(message = "Amount can not be null")
    @DecimalMin(value = "0", inclusive = false, message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Target account id can not be null")
    private Long targetAccountId;

}