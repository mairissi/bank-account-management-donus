package br.com.bank.account.management.api.donus.model;

import br.com.bank.account.management.api.donus.common.enums.Bank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Account")
public class
Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String bankCode;

    //As we have only one bank its important to have uniques accountNumber
    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateOpened;

    @Column
    private OffsetDateTime dateClosed;

    @Column(nullable = false)
    private Boolean active;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", nullable = false,
            foreignKey = @ForeignKey(name = "account_customer_fk"))
    private Customer customer;

    public Account(Customer customer){
        this.bankCode = Bank.AGENCY.value;
        this.accountNumber = RandomStringUtils.randomNumeric(10);
        this.balance = BigDecimal.ZERO;
        this.dateOpened = OffsetDateTime.now();
        this.active = true;
        this.customer = customer;
    }

    public void addBalance(BigDecimal balance) {
        this.balance = this.balance.add(balance);
    }

    public void subtractBalance(BigDecimal balance) {
        this.balance = this.balance.subtract(balance);
    }
}
