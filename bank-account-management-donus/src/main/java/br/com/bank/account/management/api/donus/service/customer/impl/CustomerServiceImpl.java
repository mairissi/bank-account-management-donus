package br.com.bank.account.management.api.donus.service.customer.impl;

import br.com.bank.account.management.api.donus.model.Customer;
import br.com.bank.account.management.api.donus.repository.CustomerRepository;
import br.com.bank.account.management.api.donus.service.customer.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Optional<Customer> findByCpf(String cpf) {
        String regex = "([0-9]{3}[.]?[0-9]{3}[.]?[0-9]{3}-[0-9]{2})";

        if(cpf.matches(regex)){
            cpf = cpf.replaceAll("[.-]", "");
        }
        return customerRepository.findByCpf(cpf);
    }

    @Override
    public Customer createOrUpdate(Customer customer) {
        String regex = "([0-9]{3}[.]?[0-9]{3}[.]?[0-9]{3}-[0-9]{2})";
        String cpf = customer.getCpf();

        if(cpf.matches(regex)){
            customer.setCpf(cpf.replaceAll("[.-]", ""));
        }

        return customerRepository.save(customer);
    }
}
