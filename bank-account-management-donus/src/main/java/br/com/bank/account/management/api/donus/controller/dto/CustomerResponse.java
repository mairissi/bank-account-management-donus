package br.com.bank.account.management.api.donus.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerResponse {
    private String cpf;
    private String fullName;
}
