package br.com.bank.account.management.api.donus.unit.repository;

import br.com.bank.account.management.api.donus.model.Account;
import br.com.bank.account.management.api.donus.model.Customer;
import br.com.bank.account.management.api.donus.model.Transaction;
import br.com.bank.account.management.api.donus.repository.TransactionRepository;
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
public class TransactionRepositoryTest extends LoadDataTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void whenValidId_thenReturnTransaction() {

        //given
        Customer customer = newUser("337.474.260-26", "Maisa Rissi");
        Account account = newAccount(customer);
        Transaction transaction =  newTransaction(account);

        // when
        Optional<Transaction> found = transactionRepository.findById(transaction.getId());

        // then
        assertTrue(found.isPresent());
        assertThat(found.get().getId())
                .isEqualTo(transaction.getId());
    }

    @Test
    public void whenValidAccountId_thenReturnAllAccountsTransactions() {

        //given
        Customer customer = newUser("337.474.260-26", "Maisa Rissi");
        Account account = newAccount(customer);
        newTransaction(account);
        newTransaction(account);

        // when
        List<Transaction> found = transactionRepository.findByAccountId(account.getId());

        // then
        assertThat(found.size()).isEqualTo(2);
        assertTrue(found
                .stream()
                .allMatch(transaction -> transaction.getAccount().getId().equals(account.getId())));
    }

    @Test
    public void whenFindAll_thenReturnAllTransactions() {

        //given
        Customer customer = newUser("337.474.260-26", "Maisa Rissi");
        Account account = newAccount(customer);
        Transaction transaction = newTransaction(account);

        Customer customer2 = newUser("148.535.350-55", "Rissi Maisa");
        Account account2 = newAccount(customer2);
        Transaction transaction2 = newTransaction(account2);

        // when
        List<Transaction> found = transactionRepository.findAll();

        // then
        assertThat(found.size()).isEqualTo(2);
        assertThat(found.get(0)).isSameAs(transaction);
        assertThat(found.get(1)).isSameAs(transaction2);
    }

}
