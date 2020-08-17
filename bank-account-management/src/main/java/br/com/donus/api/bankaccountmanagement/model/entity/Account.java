package br.com.donus.api.bankaccountmanagement.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "Account")
public class Account {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String bankCode;

    @Column(nullable = false)
    private String accountNumber; //left 0 matters

    @Column(nullable = false, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false)
    private OffsetDateTime dateOpened;

    @Column
    private OffsetDateTime dateClosed;

    @Column(nullable = false)
    private Boolean active;

    @Column(unique = true, nullable = false)
    @JoinColumn(name = "customer_id", nullable = false,
            foreignKey = @ForeignKey(name = "account_customer_fk"))
    private Customer customer;

    public void addBalance(BigDecimal balance) {
        balance = balance.add(balance);
    }

    public void subtractBalance(BigDecimal balance) {
        balance = balance.subtract(balance);
    }

}
