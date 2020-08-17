package br.com.bank.account.management.api.donus.unit.controller;

import br.com.bank.account.management.api.donus.common.enums.TransactionType;
import br.com.bank.account.management.api.donus.controller.TransactionController;
import br.com.bank.account.management.api.donus.model.Account;
import br.com.bank.account.management.api.donus.model.Transaction;
import br.com.bank.account.management.api.donus.service.transaction.TransactionService;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static br.com.bank.account.management.api.donus.unit.common.CreateObjectUtil.createAccount;
import static br.com.bank.account.management.api.donus.unit.common.CreateObjectUtil.createTransaction;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TransactionController.class)
@AutoConfigureTestDatabase
public class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    TransactionService transactionService;

    @Test
    public void whenValidTransactionId_thenReturnTransaction() throws Exception {
        Long id = 1L;
        Account account = createAccount(id, "33747426026", "Maisa Rissi", BigDecimal.TEN);
        Transaction transaction = createTransaction(id, account, BigDecimal.TEN, TransactionType.DEPOSIT.name(), BigDecimal.TEN);

        Mockito.when(transactionService.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(transaction));

        mockMvc.perform(get("/transactions/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void givenTransactions_whenFindAll_thenReturnTransactions() throws Exception {

        Account account = createAccount(1L, "33747426026", "Maisa Rissi", BigDecimal.TEN);
        Account account2 = createAccount(2L, "91647155070", "Maisa Rissi 2", BigDecimal.ZERO);
        Transaction transaction = createTransaction(1L, account, BigDecimal.TEN, TransactionType.DEPOSIT.name(), BigDecimal.TEN);
        Transaction transaction2 = createTransaction(2L, account2, BigDecimal.TEN, TransactionType.DEPOSIT.name(), BigDecimal.TEN);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        transactions.add(transaction2);

        Mockito.when(transactionService.findAll()).thenReturn(transactions);

        mockMvc.perform(get("/transactions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].accountNumber", is(account.getAccountNumber())))
                .andExpect(jsonPath("$[1].accountNumber", is(account2.getAccountNumber())));
    }

    @Test
    public void whenValidAccountId_thenTransactionsShouldBeFound() throws Exception {
        Account account = createAccount(1L, "33747426026", "Maisa Rissi", BigDecimal.TEN);
        Transaction transaction = createTransaction(1L, account, BigDecimal.TEN, TransactionType.DEPOSIT.name(), BigDecimal.TEN);
        Transaction transaction2 = createTransaction(2L, account, BigDecimal.TEN, TransactionType.DEPOSIT.name(), BigDecimal.TEN);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        transactions.add(transaction2);

        Mockito.when(transactionService.findByAccountId(Mockito.anyLong())).thenReturn(transactions);

        mockMvc.perform(get("/transactions/accounts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].accountNumber", is(account.getAccountNumber())))
                .andExpect(jsonPath("$[1].accountNumber", is(account.getAccountNumber())));
    }

    @Test
    public void whenInvalidId_thenCustomErrorShouldBeThrown() throws Exception {

        Mockito.when(transactionService.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/transactions/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Transaction not found for id:1")));
    }

    @Test
    public void whenInvalidAccount_thenCustomErrorShouldBeThrown() throws Exception {

        Mockito.when(transactionService.findByAccountId(Mockito.anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/transactions/accounts/4")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Not found transactions for accountId: 4")));
    }

    @Test
    public void whenAccountIdNull_thenCustomErrorShouldBeThrown() throws Exception {

        mockMvc.perform(get("/transactions/accounts/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
