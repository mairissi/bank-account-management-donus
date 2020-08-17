package br.com.bank.account.management.api.donus.unit.controller;

import br.com.bank.account.management.api.donus.common.enums.TransactionType;
import br.com.bank.account.management.api.donus.controller.AccountController;
import br.com.bank.account.management.api.donus.controller.dto.CustomerRequest;
import br.com.bank.account.management.api.donus.controller.dto.TransactionRequest;
import br.com.bank.account.management.api.donus.controller.dto.TransferRequest;
import br.com.bank.account.management.api.donus.model.Account;
import br.com.bank.account.management.api.donus.model.Customer;
import br.com.bank.account.management.api.donus.model.Transaction;
import br.com.bank.account.management.api.donus.service.account.AccountService;
import br.com.bank.account.management.api.donus.service.customer.CustomerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static br.com.bank.account.management.api.donus.unit.common.CreateObjectUtil.createAccount;
import static br.com.bank.account.management.api.donus.unit.common.CreateObjectUtil.createTransaction;
import static br.com.bank.account.management.api.donus.unit.common.JsonUtil.toJson;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
@AutoConfigureTestDatabase
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AccountService accountService;

    @MockBean
    CustomerService customerService;

    @Test
    public void givenAccounts_whenFindAll_thenReturnAccounts() throws Exception {
        Account account = createAccount(1L, "33747426026", "Maisa Rissi", BigDecimal.TEN);
        Account account2 = createAccount(2L, "68703748057", "Maisa Rissi 2", BigDecimal.ZERO);

        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        accounts.add(account2);

        Mockito.when(accountService.findAll()).thenReturn(accounts);

        mockMvc.perform(get("/accounts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].customerCpf", is(account.getCustomer().getCpf())))
                .andExpect(jsonPath("$[1].customerCpf", is(account2.getCustomer().getCpf())));
    }

    @Test
    public void whenValidAccountId_thenReturnAccount() throws Exception {
        Account account = createAccount(1L, "33747426026", "Maisa Rissi", BigDecimal.TEN);

        Mockito.when(accountService.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(account));

        mockMvc.perform(get("/accounts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerCpf", is(account.getCustomer().getCpf())));
    }

    @Test
    public void whenValidAccountNumber_thenReturnAccount() throws Exception {
        Account account = createAccount(1L, "33747426026", "Maisa Rissi", BigDecimal.TEN);
        String accountNumber = account.getAccountNumber();

        Mockito.when(accountService.findByAccountNumber(Mockito.anyString())).thenReturn(java.util.Optional.of(account));

        mockMvc.perform(get("/accounts/accountNumber/" + accountNumber)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber", is(accountNumber)));
    }

    @Test
    public void whenValidUsersCpf_thenReturnAccount() throws Exception {
        String cpf = "33747426026";
        Customer customer = new Customer(1L, cpf, "Maisa Rissi");
        Account account = createAccount(1L, customer, BigDecimal.TEN);

        Mockito.when(customerService.findByCpf(Mockito.anyString())).thenReturn(java.util.Optional.of(customer));
        Mockito.when(accountService.findByCustomer(Mockito.any())).thenReturn(java.util.Optional.of(account));

        mockMvc.perform(get("/accounts/cpf/" + cpf)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerCpf", is(cpf)));
    }

    @Test
    public void whenValidInput_thenCreateAccount() throws Exception {
        String cpf = "33747426026";
        String fullName = "Maisa Rissi";
        Customer customer = new Customer(1L, cpf, fullName);
        Account account = createAccount(1L, customer, BigDecimal.TEN);
        CustomerRequest body = new CustomerRequest();
        body.setCpf(cpf);
        body.setFullName(fullName);

        Mockito.when(customerService.createOrUpdate(Mockito.any())).thenReturn(customer);
        Mockito.when(accountService.create(Mockito.any())).thenReturn(account);

        mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").doesNotHaveJsonPath());

    }

    @Test
    public void whenValidAccount_thenTransfer() throws Exception {
        Account account = createAccount(1L, "33747426026", "Maisa Rissi", BigDecimal.TEN);
        Account account2 = createAccount(2L, "68703748057", "Maisa Rissi 2", BigDecimal.ZERO);

        Transaction transaction = createTransaction(1L, account, BigDecimal.TEN.negate(), TransactionType.TRANSFER.name(), BigDecimal.ZERO);
        Transaction transaction2 = createTransaction(2L, account2, BigDecimal.TEN, TransactionType.TRANSFER.name(), BigDecimal.TEN);

        TransferRequest body = new TransferRequest();
        body.setAmount(BigDecimal.TEN);
        body.setTargetAccountId(account2.getId());

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        transactions.add(transaction2);

        Mockito.when(accountService.transfer(Mockito.anyLong(), Mockito.any(), Mockito.anyLong())).thenReturn(transactions);

        mockMvc.perform(post("/accounts/1/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountNumber", is(account.getAccountNumber())))
                .andExpect(jsonPath("$[0].typeOperation", is(TransactionType.TRANSFER.name())))
                .andExpect(jsonPath("$[1].accountNumber", is(account2.getAccountNumber())))
                .andExpect(jsonPath("$[1].typeOperation", is(TransactionType.TRANSFER.name())));

    }

    @Test
    public void whenValidAccount_thenWithdraw() throws Exception {
        Account account = createAccount(1L, "33747426026", "Maisa Rissi", BigDecimal.TEN);

        Transaction transaction = createTransaction(1L, account, BigDecimal.TEN.negate(), TransactionType.WITHDRAW.name(), BigDecimal.ZERO);

        TransactionRequest body = new TransactionRequest();
        body.setAmount(BigDecimal.TEN);

        Mockito.when(accountService.withdrawTransaction(Mockito.anyLong(), Mockito.any())).thenReturn(transaction);

        mockMvc.perform(post("/accounts/1/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber", is(account.getAccountNumber())))
                .andExpect(jsonPath("$.typeOperation", is(TransactionType.WITHDRAW.name())));

    }

    @Test
    public void whenValidAccount_thenDeposit() throws Exception {
        Account account = createAccount(1L, "33747426026", "Maisa Rissi", BigDecimal.TEN);

        Transaction transaction = createTransaction(1L, account, BigDecimal.TEN.negate(), TransactionType.DEPOSIT.name(), BigDecimal.ZERO);

        TransactionRequest body = new TransactionRequest();
        body.setAmount(BigDecimal.TEN);

        Mockito.when(accountService.withdrawTransaction(Mockito.anyLong(), Mockito.any())).thenReturn(transaction);

        mockMvc.perform(post("/accounts/1/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber", is(account.getAccountNumber())))
                .andExpect(jsonPath("$.typeOperation", is(TransactionType.DEPOSIT.name())));

    }

}
