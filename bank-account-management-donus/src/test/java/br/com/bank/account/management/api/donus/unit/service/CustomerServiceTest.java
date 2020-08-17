package br.com.bank.account.management.api.donus.unit.service;

import br.com.bank.account.management.api.donus.model.Customer;
import br.com.bank.account.management.api.donus.repository.CustomerRepository;
import br.com.bank.account.management.api.donus.service.customer.CustomerService;
import br.com.bank.account.management.api.donus.service.customer.impl.CustomerServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @MockBean
    private CustomerRepository customerRepository;

    @TestConfiguration
    static class CustomerServiceImplTestContextConfiguration {

        @Bean
        public CustomerService customerService() {
            return new CustomerServiceImpl();
        }
    }

    @Test
    public void whenValidId_thenCustomerShouldBeFound() {
        Long id = 1L;
        Customer customer = new Customer(id,"33747426026", "Maisa Rissi");

        Mockito.when(customerRepository.findById(id))
                .thenReturn(java.util.Optional.of(customer));

        Optional<Customer> found = customerService.findById(id);

        assertTrue(found.isPresent());
        assertSame(found.get(), customer);
    }

    @Test
    public void whenValidCPF_thenCustomerShouldBeFound() {
        Long id = 1L;
        Customer customer = new Customer(id,"33747426026", "Maisa Rissi");

        Mockito.when(customerRepository.findByCpf(customer.getCpf()))
                .thenReturn(java.util.Optional.of(customer));

        Optional<Customer> found = customerService.findByCpf(customer.getCpf());

        assertTrue(found.isPresent());
        assertSame(found.get(), customer);
    }

    @Test
    public void whenFindAll_thenReturnAllCustomers() {
        Customer customer = new Customer(1L, "33747426026", "Maisa Rissi");
        Customer customer2 = new Customer(2L, "91647155070", "Maisa Rissi 2");
        Customer customer3 = new Customer(3L, "82820764061", "Maisa Rissi 3");

        List<Customer> allCustomers = Arrays.asList(customer, customer2, customer3);
        Mockito.when(customerRepository.findAll())
                .thenReturn(allCustomers);

        List<Customer> found = customerService.findAll();

        assertThat(found)
                .hasSize(3)
                .extracting(Customer::getCpf)
                .contains(customer.getCpf(), customer2.getCpf(), customer3.getCpf());
    }

    @Test
    public void whenValidCustomer_thenSouldBeCreate(){
        Customer customer = new Customer(1L, "33747426026", "Maisa Rissi");

        Mockito.when(customerRepository.save(customer))
                .thenReturn(customer);

        Customer found = customerService.createOrUpdate(customer);

        assertSame(found, customer);
    }

    @Test
    public void whenInvalidId_thenShouldBeReturnEmptyList() {
        Long wrong_id = 1L;

        Mockito.when(customerRepository.findById(wrong_id))
                .thenReturn(java.util.Optional.empty());

        Optional<Customer> found = customerService.findById(wrong_id);

        assertTrue(found.isEmpty());
    }

    @Test
    public void whenInvalidCpf_thenShouldBeReturnEmptyList() {
        String wrong_cpf = "33747426026";

        Mockito.when(customerRepository.findByCpf(wrong_cpf))
                .thenReturn(java.util.Optional.empty());

        Optional<Customer> found = customerService.findByCpf(wrong_cpf);

        assertTrue(found.isEmpty());
    }

    @Test
    public void whenFindAll_thenReturnEmptyList() {
        Mockito.when(customerRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<Customer> found = customerService.findAll();

        assertTrue(found.isEmpty());
    }
}
