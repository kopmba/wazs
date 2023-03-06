package com.waza.wazs.account;

import com.waza.wazs.context.AccountContext;

import java.util.List;

public class UserAccount {

    private String username;
    private String password;
    private String token;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String country;
    private String city;
    private double balance;
    private AccountContext accountContext;

    private List<ContractAccount> accounts;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public AccountContext getAccountContext() {
        return accountContext;
    }

    public void setAccountContext(AccountContext accountContext) {
        this.accountContext = accountContext;
    }

    public List<ContractAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<ContractAccount> accounts) {
        this.accounts = accounts;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", balance=" + balance +
                '}';
    }

    /**
     * Add a contractaccount in the list of contract managed by some user
     * having an account.
     * @param user
     */
    public void add(ContractAccount user) {
        accounts.add(user);
    }

    public void remove(ContractAccount user) {
        for (ContractAccount account : accounts) {
            if(user.getUsername() == account.getUsername()) {
                accounts.remove(account);
            }
        }
    }

    public ContractAccount get(String username) {
        for (ContractAccount account : accounts) {
            return account.getUsername() == username ? account : null;
        }
        return null;
    }


}
