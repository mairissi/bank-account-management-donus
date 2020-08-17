package br.com.bank.account.management.api.donus.unit.common;

import br.com.bank.account.management.api.donus.common.enums.Bank;
import br.com.bank.account.management.api.donus.model.Account;
import br.com.bank.account.management.api.donus.model.Customer;
import br.com.bank.account.management.api.donus.model.Transaction;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;

public class CreateObjectUtil {
    public static Account createAccount(Long id, String cpf, String fullName, BigDecimal balance) {

        Customer customer = new Customer(id, cpf, fullName);

        return new Account(
                id,
                Bank.AGENCY.getValue(),
                RandomStringUtils.randomNumeric(10),
                balance,
                OffsetDateTime.now(),
                null,
                true,
                customer);
    }

    public static Account createAccount(Long id, Customer customer, BigDecimal balance) {
        return new Account(
                id,
                Bank.AGENCY.getValue(),
                RandomStringUtils.randomNumeric(10),
                balance,
                OffsetDateTime.now(),
                null,
                true,
                customer);
    }

    public static Transaction createTransaction(Long id, Account account, BigDecimal amount, String typeOperation, BigDecimal balance){
        return new Transaction(id,
                account,
                OffsetDateTime.now(),
                amount.setScale(2, RoundingMode.HALF_UP),
                typeOperation,
                balance.setScale(2, RoundingMode.HALF_UP));
    }
}
