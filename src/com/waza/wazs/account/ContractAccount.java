package com.waza.wazs.account;

import java.util.List;

public class ContractAccount extends UserAccount {
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
    private String contractRef;


    public ContractAccount() {
    }

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

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public String getContractRef() {
        return contractRef;
    }

    public void setContractRef(String contractRef) {
        this.contractRef = contractRef;
    }

    @Override
    public String toString() {
        return "ContractAccount{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", balance=" + balance +
                '}';
    }


}
