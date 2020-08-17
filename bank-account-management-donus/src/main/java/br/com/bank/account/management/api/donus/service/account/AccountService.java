package br.com.bank.account.management.api.donus.service.account;

import br.com.bank.account.management.api.donus.model.Account;
import br.com.bank.account.management.api.donus.model.Customer;
import br.com.bank.account.management.api.donus.model.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public interface AccountService {

    Account create(Customer customer);
    List<Account> findAll();
    Optional<Account> findById(Long id);
    Optional<Account> findByAccountNumber(String accountNumber);
    Optional<Account> findByCustomer (Customer customer);
    List<Transaction> transfer(Long id, BigDecimal amount, Long targetAccountId);
    Transaction withdrawTransaction(Long id, BigDecimal amount);
    Transaction depositTransaction(Long id, BigDecimal amount);
}
