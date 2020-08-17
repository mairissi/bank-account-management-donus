package br.com.donus.api.bankaccountmanagement.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Data
@Table(name = "Transaction")
public class Transaction {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JoinColumn(name = "account_id", nullable = false,
            foreignKey = @ForeignKey(name = "transaction_account_fk"))
    private Account account;

    @Column(nullable = false)
    private OffsetDateTime dateTransation;

    @Column(nullable = false, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String typeOperation;

    @Column(nullable = false)
    private BigDecimal balance;
}
