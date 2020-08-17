package br.com.bank.account.management.api.donus.unit.common;

import br.com.bank.account.management.api.donus.common.enums.TransactionType;
import br.com.bank.account.management.api.donus.model.Account;
import br.com.bank.account.management.api.donus.model.Customer;
import br.com.bank.account.management.api.donus.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@DataJpaTest
public class LoadDataTest {

    @Autowired
    private TestEntityManager entityManager;

    protected Customer newUser(String cpf, String fullName) {
        Customer customer = new Customer(cpf, fullName);
        entityManager.persist(customer);
        entityManager.flush();
        return customer;
    }

    protected Account newAccount(Customer customer) {
        Account account = new Account(customer);
        entityManager.persist(account);
        entityManager.flush();
        return account;
    }

    protected Transaction newTransaction(Account account) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(new BigDecimal("150.00"));
        transaction.setDateTransaction(OffsetDateTime.now());
        transaction.setBalance(new BigDecimal("1250.00"));
        transaction.setTypeOperation(TransactionType.TRANSFER.name());

        entityManager.persist(transaction);
        entityManager.flush();

        return transaction;
    }
}
