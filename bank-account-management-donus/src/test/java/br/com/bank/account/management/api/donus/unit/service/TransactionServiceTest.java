package br.com.bank.account.management.api.donus.unit.service;

import br.com.bank.account.management.api.donus.common.enums.Bank;
import br.com.bank.account.management.api.donus.common.enums.TransactionType;
import br.com.bank.account.management.api.donus.model.Account;
import br.com.bank.account.management.api.donus.model.Customer;
import br.com.bank.account.management.api.donus.model.Transaction;
import br.com.bank.account.management.api.donus.repository.TransactionRepository;
import br.com.bank.account.management.api.donus.service.transaction.TransactionService;
import br.com.bank.account.management.api.donus.service.transaction.impl.TransactionServiceImpl;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static br.com.bank.account.management.api.donus.unit.common.CreateObjectUtil.createAccount;
import static br.com.bank.account.management.api.donus.unit.common.CreateObjectUtil.createTransaction;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @MockBean
    private TransactionRepository transactionRepository;

    @TestConfiguration
    static class TransactionServiceImplTestContextConfiguration {

        @Bean
        public TransactionService transactionService() {
            return new TransactionServiceImpl();
        }
    }

    @Test
    public void whenValidId_thenTransactionShouldBeFound() {
        Long id = 1L;
        Account account = createAccount(id, "33747426026", "Maisa Rissi", BigDecimal.TEN);
        Transaction transaction = createTransaction(id, account, BigDecimal.TEN, TransactionType.DEPOSIT.name(), BigDecimal.TEN);

        Mockito.when(transactionRepository.findById(id))
                .thenReturn(java.util.Optional.of(transaction));

        Optional<Transaction> found = transactionService.findById(id);

        assertTrue(found.isPresent());
        assertSame(found.get(), transaction);
    }

    @Test
    public void whenFindAll_thenReturnAllTransactions() {
        Account account = createAccount(1L, "33747426026", "Maisa Rissi", BigDecimal.TEN);
        Account account2 = createAccount(2L, "91647155070", "Maisa Rissi 2", BigDecimal.ZERO);
        Transaction transaction = createTransaction(1L, account, BigDecimal.TEN, TransactionType.DEPOSIT.name(), BigDecimal.TEN);
        Transaction transaction2 = createTransaction(2L, account2, BigDecimal.TEN, TransactionType.DEPOSIT.name(), BigDecimal.TEN);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        transactions.add(transaction2);

        Mockito.when(transactionRepository.findAll())
                .thenReturn(transactions);

        List<Transaction> found = transactionService.findAll();

        assertThat(found)
                .hasSize(2)
                .extracting(Transaction::getAccount)
                .contains(account, account2);
    }

    @Test
    public void whenValidAccounId_thenAccountsTransactionsShouldBeFound() {
        Long id = 1L;
        Account account = createAccount(id, "33747426026", "Maisa Rissi", BigDecimal.TEN);
        Transaction transaction = createTransaction(id, account, BigDecimal.TEN, TransactionType.DEPOSIT.name(), BigDecimal.TEN);
        Transaction transaction2 = createTransaction(id, account, BigDecimal.TEN, TransactionType.DEPOSIT.name(), BigDecimal.TEN);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        transactions.add(transaction2);

        Mockito.when(transactionRepository.findByAccountId(id))
                .thenReturn(transactions);

        List<Transaction> found = transactionService.findByAccountId(id);

        assertThat(found)
                .hasSize(2)
                .extracting(Transaction::getAccount)
                .contains(account);
    }

    @Test
    public void whenInvalidId_thenShouldBeReturnEmptyList() {
        Long wrong_id = 1L;

        Mockito.when(transactionService.findById(wrong_id))
                .thenReturn(java.util.Optional.empty());

        Optional<Transaction> found = transactionService.findById(wrong_id);

        assertTrue(found.isEmpty());
    }

    @Test
    public void whenInvalidCpf_thenShouldBeReturnEmptyList() {
        Long wrong_account = 1L;

        Mockito.when(transactionService.findByAccountId(wrong_account))
                .thenReturn(Collections.emptyList());

        List<Transaction> found = transactionService.findByAccountId(wrong_account);

        assertTrue(found.isEmpty());
    }

    @Test
    public void whenFindAll_thenReturnEmptyList() {
        Mockito.when(transactionService.findAll())
                .thenReturn(Collections.emptyList());

        List<Transaction> found = transactionService.findAll();

        assertTrue(found.isEmpty());
    }
}
