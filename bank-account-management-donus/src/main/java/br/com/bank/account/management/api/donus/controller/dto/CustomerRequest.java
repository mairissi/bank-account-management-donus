package br.com.bank.account.management.api.donus.controller.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CustomerRequest {

    @NotNull(message = "CPF can not be null")
    @NotBlank(message = "CPF can not be blank")
    @CPF(message = "CPF must be a valid number")
    private String cpf;

    @NotNull(message = "Full name can not be null")
    @NotBlank(message = "Full name can not be blank")
    private String fullName;
}