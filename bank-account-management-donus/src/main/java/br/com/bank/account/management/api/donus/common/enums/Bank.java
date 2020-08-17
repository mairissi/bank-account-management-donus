package br.com.bank.account.management.api.donus.common.enums;

/**
 * The idea is to have bank agencies in the repository.
 * But for now, we have only internal transfer
 * So I decided to use a simple enum to do it
 */
public enum Bank {
    AGENCY("123");

    public String value;

    Bank(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}