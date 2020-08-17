package br.com.donus.api.bankaccountmanagement.model;

import lombok.AllArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@Table(name = "Customer")
public class Customer {

    @Id
    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String fullName;
}
