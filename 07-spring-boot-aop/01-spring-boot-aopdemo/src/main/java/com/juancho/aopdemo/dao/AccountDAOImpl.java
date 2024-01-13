package com.juancho.aopdemo.dao;

import com.juancho.aopdemo.Account;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountDAOImpl implements AccountDAO {

    private String name;

    private String serviceCode;

    @Override
    public void addAccount(Account account) {
        System.out.println(getClass() + ": Doing my work: Adding and account");
    }

    public String getName() {
        System.out.println(getClass() + ": getName()");
        return name;
    }

    public void setName(String name) {
        System.out.println(getClass() + ": setName()");
        this.name = name;
    }

    public String getServiceCode() {
        System.out.println(getClass() + ": getServiceName()");
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        System.out.println(getClass() + ": setServiceCode()");
        this.serviceCode = serviceCode;
    }

    @Override
    public List<Account> findAccounts() {
        return findAccounts(false);
    }

    @Override
    public List<Account> findAccounts(boolean tripWire) {
        // for academic purposes simulate an exception
        if (tripWire) {
            throw new RuntimeException("No soup for you!");
        }

        List<Account> accounts = new ArrayList<>();
        Account accountOne = new Account("Pepe", "Admin");
        Account accountTwo = new Account("Pito", "Silver");
        Account accountThree = new Account("Pedro", "Platinum");
        accounts.add(accountOne);
        accounts.add(accountTwo);
        accounts.add(accountThree);

        return accounts;
    }

}
