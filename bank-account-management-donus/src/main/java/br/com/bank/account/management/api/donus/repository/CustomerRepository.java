package br.com.bank.account.management.api.donus.repository;

import br.com.bank.account.management.api.donus.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByCpf(String cpf);
}
