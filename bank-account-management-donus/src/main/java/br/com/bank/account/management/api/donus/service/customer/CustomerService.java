package br.com.bank.account.management.api.donus.service.customer;

import br.com.bank.account.management.api.donus.model.Customer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CustomerService {
    List<Customer> findAll();
    Optional<Customer> findById(Long id);
    Optional<Customer> findByCpf(String cpf);
    Customer createOrUpdate (Customer customer);
}
