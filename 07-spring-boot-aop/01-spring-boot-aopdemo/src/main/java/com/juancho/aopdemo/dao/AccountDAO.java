package com.juancho.aopdemo.dao;

import com.juancho.aopdemo.Account;

import java.util.List;

public interface AccountDAO {

    void addAccount(Account account);

    String getName();

    void setName(String name);

    String getServiceCode();

    void setServiceCode(String serviceCode);

    List<Account> findAccounts();

    List<Account> findAccounts(boolean tripWire);

}
