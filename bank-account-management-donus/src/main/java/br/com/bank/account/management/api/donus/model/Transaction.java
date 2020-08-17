package br.com.bank.account.management.api.donus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "Transaction")
public class Transaction implements Serializable {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false,
            foreignKey = @ForeignKey(name = "transaction_account_fk"))
    private Account account;

    @Column(nullable = false)
    private OffsetDateTime dateTransaction;

    @Column(nullable = false, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String typeOperation;

    @Column(nullable = false)
    private BigDecimal balance;
}
