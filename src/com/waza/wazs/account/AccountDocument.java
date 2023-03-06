package com.waza.wazs.account;

import com.waza.wazs.broker.BrokerFactory;
import com.waza.wazs.broker.SharedBroker;
import com.waza.wazs.context.handler.ConcreteHandler;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class AccountDocument {

    /**
     * Create a new contract account by providing the balance
     * @param user
     * @param path
     * @param amount
     * @param requestOptions
     * @throws FileNotFoundException
     */
    public void create(ContractAccount user, String path, double amount, String... requestOptions) throws FileNotFoundException {
        //call the broker factory to get the data from serialization
        BrokerFactory bf = new BrokerFactory();
        Object result = bf.get(path);

        if (result != null) {
            result = new ArrayList<>();

            //find the corresponding user account compare and add the contract
            UserAccount ua = findAccount(requestOptions[0], requestOptions[1]);
            if(ua != null && result instanceof List) {
                if(ua.getUsername() == user.getContractRef()) {
                    ua.setBalance(ua.getBalance() + amount);
                    ua.add(user);
                }
                ((List) result).add(ua);
            }
        } else {
            if (result instanceof List) {
                for (int i = 0; i < ((List) result).size(); i++) {
                    if (((List) result).get(i) instanceof UserAccount) {
                        UserAccount ua = (UserAccount) ((List) result).get(i);
                        if (ua.getUsername() == user.getContractRef()) {
                            ua.setBalance(ua.getBalance() + amount);
                            ua.add(user);
                        }
                    }
                }
            }
        }

        SharedBroker broker = new SharedBroker();
        if(user.getAccountContext().getAccountant() != null) {
            broker.operation(path, user.getAccountContext().getAccountant(), result, 1);
        } else {
            broker.operation(path, user.getAccountContext().getOwner(), result, 1);
        }

    }

    /**
     * Transfer amount to the account
     * @param user
     * @param path
     * @param amount
     * @param requestOptions
     * @throws Exception
     */
    public void transfer(ContractAccount user, String path, double amount, String... requestOptions) throws Exception {
        //call the broker factory to get the data from serialization
        BrokerFactory bf = new BrokerFactory();
        Object result = bf.get(path);

        if (result != null) {

            //throw an exception to store data before using the function
            throw new Exception("Create an object account in the broker before using the function");


        } else {
            if (result instanceof List) {
                for (int i = 0; i < ((List) result).size(); i++) {
                    if (((List) result).get(i) instanceof UserAccount) {
                        UserAccount ua = (UserAccount) ((List) result).get(i);
                        if (ua.getUsername() == user.getContractRef()) {
                            ua.setBalance(ua.getBalance() + amount);
                            user.setBalance(user.getBalance() + amount);
                        }
                    }
                }
            }
        }

        SharedBroker broker = new SharedBroker();
        if(user.getAccountContext().getAccountant() != null) {
            broker.operation(path, user.getAccountContext().getAccountant(), result, 1);
        } else {
            broker.operation(path, user.getAccountContext().getOwner(), result, 1);
        }

    }

    /**
     * Retrieve money from account
     * @param user
     * @param path
     * @param amount
     * @param requestOptions
     * @return
     * @throws Exception
     */
    public double withdraw(ContractAccount user, String path,
                           double amount, String... requestOptions) throws Exception {
        double balance = 0;
        //call the broker factory to get the data from
        BrokerFactory bf = new BrokerFactory();
        Object result = bf.get(path);

        if (result != null) {
            //throw an exception to store data before using the function
            throw new Exception("Create an object account in the broker before using the function");
        } else {
            if(result instanceof List) {
                for (int i = 0; i < ((List) result).size() ; i++) {
                    if(((List) result).get(i) instanceof UserAccount) {
                        UserAccount ua = (UserAccount) ((List) result).get(i);
                        if(ua.getUsername() == user.getContractRef()) {
                            ua.setBalance(ua.getBalance() - amount);
                            user.setBalance(user.getBalance() - amount);
                            balance = balance + user.getBalance();
                        }
                    }
                }
            }
        }

        SharedBroker broker = new SharedBroker();
        if(user.getAccountContext().getAccountant() != null) {
            broker.operation(path, user.getAccountContext().getAccountant(), result, 1);
        } else {
            broker.operation(path, user.getAccountContext().getOwner(), result, 1);
        }

        return balance;
    }

    /**
     * Find the data corresponding to the useraccount managing the application context
     * from the remote broker sharing resource.
     * @param url
     * @param path
     * @return
     * @throws FileNotFoundException
     */
    public UserAccount findAccount(String url, String... path) throws FileNotFoundException {
        ConcreteHandler handler = new ConcreteHandler<>();
        //the path contains the username useraccount
        HttpResponse<String> response = handler.handleRequest(url, path[0]);
        BrokerFactory brokerFactory = new BrokerFactory();
        Object result = brokerFactory.get(response.toString().getBytes());
        if(result != null) {
            result = new ArrayList();
        }

        if(result instanceof List) {
            return (UserAccount) ((List) result).get(0);
        }
        return null;

    }
}
