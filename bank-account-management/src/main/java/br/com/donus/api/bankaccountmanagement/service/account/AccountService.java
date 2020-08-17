package br.com.donus.api.bankaccountmanagement.service;

import br.com.donus.api.bankaccountmanagement.controller.dto.AccountDTO;
import br.com.donus.api.bankaccountmanagement.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountDTO accountDTO;

    public AccountDTO findById(Long id) {
        return accountDTO.build(accountRepository.findById(id)
                .orElseThrow(NotFoundException::new));
    }
}
