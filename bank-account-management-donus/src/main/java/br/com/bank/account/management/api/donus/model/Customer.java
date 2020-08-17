package br.com.bank.account.management.api.donus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Customer")
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String fullName;

    public Customer(String cpf, String fullName) {
        this.cpf = cpf;
        this.fullName = fullName;
    }
}
