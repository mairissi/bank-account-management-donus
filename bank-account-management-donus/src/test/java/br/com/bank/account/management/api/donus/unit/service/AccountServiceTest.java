package br.com.bank.account.management.api.donus.unit.service;

import br.com.bank.account.management.api.donus.common.enums.Bank;
import br.com.bank.account.management.api.donus.common.enums.TransactionType;
import br.com.bank.account.management.api.donus.controller.dto.TransactionRequest;
import br.com.bank.account.management.api.donus.model.Account;
import br.com.bank.account.management.api.donus.model.Customer;
import br.com.bank.account.management.api.donus.model.Transaction;
import br.com.bank.account.management.api.donus.repository.AccountRepository;
import br.com.bank.account.management.api.donus.repository.TransactionRepository;
import br.com.bank.account.management.api.donus.service.account.AccountService;
import br.com.bank.account.management.api.donus.service.account.impl.AccountServiceImpl;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.NotFoundException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static br.com.bank.account.management.api.donus.unit.common.CreateObjectUtil.createAccount;
import static br.com.bank.account.management.api.donus.unit.common.CreateObjectUtil.createTransaction;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    @TestConfiguration
    static class AccountServiceImplTestContextConfiguration {

        @Bean
        public AccountService accountService() {
            return new AccountServiceImpl();
        }
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void whenValidId_thenAccountShouldBeFound() {
        Long id = 1L;
        Account account = createAccount(id,"33747426026", "Maisa Rissi", BigDecimal.ZERO);

        Mockito.when(accountRepository.findById(id))
                .thenReturn(java.util.Optional.of(account));

        Optional<Account> found = accountService.findById(id);

        assertTrue(found.isPresent());
        assertSame(found.get(), account);
    }

    @Test
    public void whenValidAccountNumber_thenAccountShouldBeFound() {
        Account account = createAccount(1L,"33747426026", "Maisa Rissi", BigDecimal.ZERO);

        Mockito.when(accountRepository
                .findByAccountNumberAndBankCodeAndActiveIsTrue(account.getAccountNumber(), account.getBankCode()))
                .thenReturn(java.util.Optional.of(account));

        Optional<Account> found = accountService.findByAccountNumber(account.getAccountNumber());

        assertTrue(found.isPresent());
        assertSame(found.get(), account);
    }

    @Test
    public void whenValidCustomer_thenAccountShouldBeFound() {
        Account account = createAccount(1L,"33747426026", "Maisa Rissi", BigDecimal.ZERO);

        Mockito.when(accountRepository
                .findByCustomer(account.getCustomer()))
                .thenReturn(java.util.Optional.of(account));

        Optional<Account> found = accountService.findByCustomer(account.getCustomer());

        assertTrue(found.isPresent());
        assertSame(found.get(), account);
    }

    @Test
    public void whenFindAll_thenReturnAllAccounts() {
        Account account = createAccount(1L, "33747426026", "Maisa Rissi", BigDecimal.ZERO);
        Account account1 = createAccount(2L, "91647155070", "Maisa Rissi 2", BigDecimal.ZERO);
        Account account2 = createAccount(3L, "82820764061", "Maisa Rissi 3", BigDecimal.ZERO);

        List<Account> allAccounts = Arrays.asList(account, account1, account2);
        Mockito.when(accountRepository.findAll())
                .thenReturn(allAccounts);

        List<Account> found = accountService.findAll();

        assertThat(found)
                .hasSize(3)
                .extracting(Account::getCustomer)
                .contains(account.getCustomer(), account1.getCustomer(), account2.getCustomer());
    }

    @Test
    public void whenValidCustomer_thenAccountShouldBeCreate() {
        Customer customer = new Customer(1L, "33747426026", "Maisa Rissi");
        Account account = new Account(customer);

        Mockito.when(accountRepository.save(Mockito.any(Account.class)))
                .thenReturn(account);

        Account found = accountService.create(customer);

        assertSame(found, account);
    }

    @Test
    public void whenValidAccount_thenDepositShouldBeDone() {
        Account account = createAccount(1L, "33747426026", "Maisa Rissi", BigDecimal.ZERO);
        Transaction transaction = createTransaction(1L, account, BigDecimal.TEN, TransactionType.DEPOSIT.name(), BigDecimal.TEN);
        TransactionRequest request = new TransactionRequest();
        request.setAmount(BigDecimal.TEN);

        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class)))
                .thenReturn(transaction);
        Mockito.when(accountRepository.findByIdAndActiveIsTrue(Mockito.anyLong()))
                .thenReturn(Optional.of(account));

        Transaction found = accountService.depositTransaction(1L, BigDecimal.TEN);

        assertSame(found, transaction);
    }

    @Test
    public void whenValidAccount_thenWithdrawShouldBeDone() {
        Account account = createAccount(1L, "33747426026", "Maisa Rissi", BigDecimal.TEN);
        Transaction transaction = createTransaction(1L, account, BigDecimal.ONE.negate(), TransactionType.WITHDRAW.name(), new BigDecimal(9));

        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class)))
                .thenReturn(transaction);
        Mockito.when(accountRepository.findByIdAndActiveIsTrue(Mockito.anyLong()))
                .thenReturn(Optional.of(account));

        Transaction found = accountService.withdrawTransaction(1L, BigDecimal.ONE);

        assertSame(found, transaction);
    }

    @Test
    public void whenValidAccount_thenTransferShouldBeDone() {
        Account account = createAccount(1L, "33747426026", "Maisa Rissi", BigDecimal.TEN);
        Account account2 = createAccount(2L, "68703748057", "Maisa Rissi 2", BigDecimal.ZERO);

        Transaction transaction = createTransaction(1L, account, BigDecimal.TEN.negate(), TransactionType.TRANSFER.name(), BigDecimal.ZERO);
        Transaction transaction2 = createTransaction(2L, account2, BigDecimal.TEN, TransactionType.TRANSFER.name(), BigDecimal.TEN);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        transactions.add(transaction2);

        Mockito.when(transactionRepository.save(Mockito.refEq(transaction, "id", "dateTransaction", "account")))
                .thenReturn(transaction);
        Mockito.when(transactionRepository.save(Mockito.refEq(transaction2, "id", "dateTransaction", "account")))
                .thenReturn(transaction2);
        Mockito.when(accountRepository.findByIdAndActiveIsTrue(account.getId()))
                .thenReturn(Optional.of(account));
        Mockito.when(accountRepository.findByIdAndActiveIsTrue(account2.getId()))
                .thenReturn(Optional.of(account2));

        List<Transaction> found = accountService.transfer(1L, BigDecimal.TEN, 2L);

        assertThat(found)
                .hasSize(2)
                .extracting(Transaction::getAmount)
                .contains(transaction.getAmount(), transaction2.getAmount());
    }

    @Test
    public void whenInvalidAccount_thenNotFoundShouldBeThrown() {
        Account account = createAccount(1L, "33747426026", "Maisa Rissi", BigDecimal.TEN);

        exception.expect(NotFoundException.class);

        Mockito.when(accountRepository.findByIdAndActiveIsTrue(account.getId()))
                .thenReturn(Optional.of(account));

        accountService.transfer(1L, BigDecimal.TEN, 2L);
    }

    @Test
    public void whenInvalidAmount_thenDataIntegrityShouldBeThrown() {

        exception.expect(DataIntegrityViolationException.class);

        Account account = createAccount(1L, "33747426026", "Maisa Rissi", BigDecimal.ONE);

        Mockito.when(accountRepository.findByIdAndActiveIsTrue(Mockito.anyLong()))
                .thenReturn(Optional.of(account));

        accountService.withdrawTransaction(1L, BigDecimal.TEN);

    }
}
