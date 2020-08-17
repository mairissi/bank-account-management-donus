package br.com.bank.account.management.api.donus.service.account.impl;

import br.com.bank.account.management.api.donus.common.enums.Bank;
import br.com.bank.account.management.api.donus.common.enums.TransactionType;
import br.com.bank.account.management.api.donus.model.Account;
import br.com.bank.account.management.api.donus.model.Customer;
import br.com.bank.account.management.api.donus.model.Transaction;
import br.com.bank.account.management.api.donus.repository.AccountRepository;
import br.com.bank.account.management.api.donus.repository.TransactionRepository;
import br.com.bank.account.management.api.donus.service.account.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Account create(Customer customer) {
        var account = new Account(customer);
        return accountRepository.save(account);
    }

    @Override
    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return accountRepository
                .findByAccountNumberAndBankCodeAndActiveIsTrue(accountNumber, Bank.AGENCY.value);
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> findByCustomer(Customer customer){
        return accountRepository.findByCustomer(customer);
    }

    @Transactional
    @Override
    public List<Transaction> transfer(Long accountId, BigDecimal amount, Long targetAccountId) {
        log.info("Start transfer method");

        List<Transaction> transactions = new ArrayList<>();

        Account sourceAccount = withdraw(accountId, amount);
        transactions.add(
                newTransaction(TransactionType.TRANSFER.name(), amount.negate(), sourceAccount));

        Account targetAccount = deposit(targetAccountId, amount);
        transactions.add(
                newTransaction(TransactionType.TRANSFER.name(), amount, targetAccount));


        log.info("Generated transactions ", transactions);
        log.info("Finish transfer method");

        return transactions;
    }

    @Override
    @Transactional
    public Transaction withdrawTransaction(Long accountId, BigDecimal amount) {
        log.info("Start withdraw transaction method");

        //The tax for withdraw is 1%
        var total = amount.multiply(new BigDecimal(1.01));

        Account account = withdraw(accountId, total);
        Transaction transaction =
                newTransaction(TransactionType.WITHDRAW.name(), total.negate(), account);

        log.info("Generated transaction ", transaction);
        log.info("Finish withdraw transaction method");

        return transaction;
    }

    @Override
    @Transactional
    public Transaction depositTransaction(Long accountId, BigDecimal amount) {
        log.info("Start deposit transaction method");

        //The bonus for deposit is 0.5%
        var total = amount.multiply(new BigDecimal(1.005));

        Account account = deposit(accountId, total);
        Transaction transaction =
                newTransaction(TransactionType.DEPOSIT.name(), total, account);

        log.info("Generated transaction ", transaction);
        log.info("Finish deposit transaction  method");

        return transaction;
    }

    @Transactional
    private Account withdraw(Long accountId, BigDecimal amount) {
        log.info("Start withdraw method");
        return accountRepository.findByIdAndActiveIsTrue(accountId)
                .map(acc -> {
                    if (amount.compareTo(acc.getBalance()) < 1){
                        acc.subtractBalance(amount);
                        accountRepository.save(acc);
                        log.info("Finish withdraw method");
                        return acc;
                    } else {
                        throw new DataIntegrityViolationException("The amount is greater than balance");
                    }
                }).orElseThrow(() -> new NotFoundException("Account not found for id: " + accountId));

    }

    @Transactional
    private Account deposit(Long accountId, BigDecimal amount) {
        log.info("Start deposit method");
        return accountRepository.findByIdAndActiveIsTrue(accountId)
                .map(acc -> {
                    acc.addBalance(amount);
                    accountRepository.save(acc);
                    log.info("Finish deposit method");
                    return acc;
                })
                .orElseThrow(() -> new NotFoundException("Account not found for id: " + accountId));
    }

    @Transactional
    private Transaction newTransaction(
            String transactionType,
            BigDecimal amount,
            Account account) {

        Transaction transaction = new Transaction();

        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setBalance(account.getBalance());
        transaction.setDateTransaction(OffsetDateTime.now());
        transaction.setTypeOperation(transactionType);

        return transactionRepository.save(transaction);
    }
}
