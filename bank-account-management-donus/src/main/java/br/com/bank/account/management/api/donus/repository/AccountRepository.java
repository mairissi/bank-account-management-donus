package br.com.bank.account.management.api.donus.repository;

import br.com.bank.account.management.api.donus.model.Account;
import br.com.bank.account.management.api.donus.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByIdAndActiveIsTrue(Long id);
    Optional<Account> findByAccountNumberAndBankCodeAndActiveIsTrue(String accountNumber, String bankCode);
    Optional<Account> findByCustomer(Customer customer);
}
