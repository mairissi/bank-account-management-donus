package br.com.bank.account.management.api.donus.service.transaction.impl;

import br.com.bank.account.management.api.donus.model.Transaction;
import br.com.bank.account.management.api.donus.repository.TransactionRepository;
import br.com.bank.account.management.api.donus.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public List<Transaction> findByAccountId(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }
}
