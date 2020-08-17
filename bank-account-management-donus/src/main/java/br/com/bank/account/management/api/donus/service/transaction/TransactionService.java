package br.com.bank.account.management.api.donus.service.transaction;

import br.com.bank.account.management.api.donus.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TransactionService {
    List<Transaction> findAll();
    Optional<Transaction> findById(Long id);
    List<Transaction> findByAccountId(Long accountId);
}
