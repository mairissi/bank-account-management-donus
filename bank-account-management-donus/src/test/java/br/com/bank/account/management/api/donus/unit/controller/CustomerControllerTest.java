package br.com.bank.account.management.api.donus.unit.controller;

import br.com.bank.account.management.api.donus.controller.CustomerController;
import br.com.bank.account.management.api.donus.model.Customer;
import br.com.bank.account.management.api.donus.service.customer.CustomerService;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static br.com.bank.account.management.api.donus.unit.common.JsonUtil.toJson;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
@AutoConfigureTestDatabase
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    @Test
    public void whenValidCustomerId_thenReturnCustomer() throws Exception {
        Customer customer = new Customer(1L, "33747426026", "Maisa Rissi");

        Mockito.when(customerService.findById(1L)).thenReturn(java.util.Optional.of(customer));

        mockMvc.perform(get("/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf", is(customer.getCpf())));
    }

    @Test
    public void givenCustomers_whenFindAll_thenReturnCustomers() throws Exception {
        Customer customer = new Customer(1L, "33747426026", "Maisa Rissi");
        Customer customer2 = new Customer(2L, "91647155070", "Maisa Rissi 2");

        List<Customer> customers = new ArrayList<>();
        customers.add(customer);
        customers.add(customer2);

        Mockito.when(customerService.findAll()).thenReturn(customers);

        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].cpf", is(customer.getCpf())))
                .andExpect(jsonPath("$[1].cpf", is(customer2.getCpf())));
    }

    @Test
    public void whenValidInput_thenUpdateCustomer() throws Exception {
        Customer customer = new Customer(1L, "33747426026", "Maisa Rissi");

        Mockito.when(customerService.findById(1L)).thenReturn(java.util.Optional.of(customer));

        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(customer)))
                .andExpect(status().isNoContent());

    }

    @Test
    public void whenValidCpf_thenReturnCustomer() throws Exception {
        Customer customer = new Customer(1L, "33747426026", "Maisa Rissi");

        Mockito.when(customerService.findByCpf("33747426026")).thenReturn(java.util.Optional.of(customer));

        mockMvc.perform(get("/users/cpf/33747426026")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf", is(customer.getCpf())));
    }

    @Test
    public void whenInvalidId_thenCustomErrorShouldBeThrown() throws Exception {

        Mockito.when(customerService.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("User not found for id:1")));
    }

    @Test
    public void whenInvalidCpf_thenCustomErrorShouldBeThrown() throws Exception {

        Mockito.when(customerService.findByCpf("33747426026")).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/cpf/33747426026")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("User not found for cpf:33747426026")));
    }

    @Test
    public void whenFindAllCustomers_thenCustomErrorShouldBeThrown() throws Exception {

        Mockito.when(customerService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Did not found any users")));
    }

    @Test
    public void whenInvalidBodyToUpdateCustomer_thenCustomErrorShouldBeThrown() throws Exception {

        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
