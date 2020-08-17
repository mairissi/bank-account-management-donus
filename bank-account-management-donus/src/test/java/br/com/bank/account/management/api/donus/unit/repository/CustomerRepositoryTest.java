package br.com.bank.account.management.api.donus.unit.repository;

import br.com.bank.account.management.api.donus.model.Customer;
import br.com.bank.account.management.api.donus.repository.CustomerRepository;
import br.com.bank.account.management.api.donus.unit.common.LoadDataTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomerRepositoryTest extends LoadDataTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void whenValidId_thenReturnUser() {

        //given
        Customer customer = newUser("33747426026", "Maisa Rissi");

        // when
        Optional<Customer> found = customerRepository.findById(customer.getId());

        // then
        assertTrue(found.isPresent());
        assertThat(found.get().getId())
                .isEqualTo(customer.getId());
    }

    @Test
    public void whenFindAll_thenReturnAllCustomers() {

        //given
        Customer customer = newUser("33747426026", "Maisa Rissi");
        Customer customer2 = newUser("14853535055", "Rissi Maisa");

        // when
        List<Customer> found = customerRepository.findAll();

        // then
        assertThat(found)
                .hasSize(2)
                .extracting(Customer::getCpf)
                .contains(customer.getCpf(), customer2.getCpf());
    }

    @Test
    public void whenValidCPF_thenReturnUser() {

        //given
        Customer customer = newUser("337.474.260-26", "Maisa Rissi");

        // when
        Optional<Customer> found = customerRepository.findByCpf(customer.getCpf());

        // then
        assertTrue(found.isPresent());
        assertSame(found.get(), customer);
    }

    @Test
    public void whenDuplicateCpf_thenCustomerShouldReturnException() {

        //given
        try {
            newUser("33747426026", "Maisa Rissi");
            newUser("33747426026", "Rissi Maisa");
            Assert.fail();
        } catch (Exception ignored) { }
    }
}
