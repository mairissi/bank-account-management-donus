package br.com.bank.account.management.api.donus.unit.repository;

import br.com.bank.account.management.api.donus.model.Account;
import br.com.bank.account.management.api.donus.model.Customer;
import br.com.bank.account.management.api.donus.repository.AccountRepository;
import br.com.bank.account.management.api.donus.unit.common.LoadDataTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTest extends LoadDataTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void whenValidId_thenReturnAccount() {

        //given
        Customer customer = newUser("337.474.260-26", "Maisa Rissi");
        Account account = newAccount(customer);

        // when
        Optional<Account> found = accountRepository.findById(account.getId());

        // then
        assertTrue(found.isPresent());
        assertThat(found.get().getId())
                .isEqualTo(account.getId());
    }

    @Test
    public void whenValidId_thenReturnOnlyActiveAccount() {

        //given
        Customer customer = newUser("337.474.260-26", "Maisa Rissi");
        Account account = newAccount(customer);

        // when
        Optional<Account> found = accountRepository.findByIdAndActiveIsTrue(account.getId());

        // then
        assertTrue(found.isPresent());
        assertThat(found.get().getId())
                .isEqualTo(account.getId());
    }

    @Test
    public void whenValidCustomer_thenReturnAccount() {

        //given
        Customer customer = newUser("337.474.260-26", "Maisa Rissi");
        Account account = newAccount(customer);

        // when
        Optional<Account> found = accountRepository.findByCustomer(customer);

        // then
        assertTrue(found.isPresent());
        assertThat(found.get().getId())
                .isEqualTo(account.getId());
    }

    @Test
    public void whenFindAll_thenReturnAllAccounts() {

        //given
        Customer customer = newUser("337.474.260-26", "Maisa Rissi");
        Account account = newAccount(customer);

        Customer customer2 = newUser("148.535.350-55", "Rissi Maisa");
        Account account2 = newAccount(customer2);

        // when
        List<Account> accounts = accountRepository.findAll();

        // then
        assertThat(accounts.size()).isEqualTo(2);
        assertThat(accounts.get(0)).isSameAs(account);
        assertThat(accounts.get(1)).isSameAs(account2);
    }

    @Test
    public void whenValidAccountNumber_thenReturnAccount() {

        //given
        Customer customer = newUser("337.474.260-26", "Maisa Rissi");
        Account account = newAccount(customer);

        // when
        Optional<Account> found = accountRepository.findByAccountNumberAndBankCodeAndActiveIsTrue(account.getAccountNumber(), "123");

        // then
        assertTrue(found.isPresent());
        assertThat(found.get()).isSameAs(account);
    }

}
