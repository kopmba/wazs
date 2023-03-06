package com.waza.wazs.context;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.waza.wazs.account.AccountDocument;
import com.waza.wazs.account.ContractAccount;
import com.waza.wazs.account.UserAccount;
import com.waza.wazs.broker.Broker;
import com.waza.wazs.broker.BrokerFactory;
import com.waza.wazs.broker.SharedBroker;
import com.waza.wazs.context.client.ApplicationContext;
import com.waza.wazs.context.client.ClientContext;
import com.waza.wazs.context.handler.ConcreteHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public abstract class AccountContext {

    private String owner, accountant;

    public static final String CREATE_MODE = "create";
    public static final String TRANSFER_MODE = "transfer";
    public static final String WITHDRAW_MODE = "transfer";

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAccountant() {
        return accountant;
    }

    public void setAccountant(String accountant) {
        this.accountant = accountant;
    }

    public abstract HttpHandler submit(AccountDocument adoc, String path, Object... o);

    //the method will provided authentication credentials for the next version
    public void notifyMessage(UserAccount a, String... path) throws FileNotFoundException {
        SharedBroker sb = new SharedBroker();
        BrokerFactory brokerFactory = new BrokerFactory();
        Object result = brokerFactory.get(path[0]);
        Object resultAccount = brokerFactory.get(path[1]);
        if(result == null) {
            result = new ArrayList<>();
        }
        if(resultAccount == null) {
            resultAccount = new ArrayList<UserAccount>();
        }
        if(resultAccount instanceof List) {
            for (Object account : (List) resultAccount) {
                if(account instanceof  UserAccount) {
                    if(((UserAccount) account).getUsername() == a.getUsername() || ((UserAccount) account).getPhone() == a.getPhone()) {
                        ContractAccount ca = a.getAccounts().get(a.getAccounts().size());
                        String message = Instant.now() + "Création de compte utilisateur " + a.getUsername() + "pour ajout de contrat utilisateur " + ca.getUsername() + " avec solde de " + ca.getBalance();
                        if(result instanceof List) {
                            ((List) result).add(message);
                        }
                    }
                }
            }
        }

        sb.operation(path[0], null, result);
    }

    public HttpHandler submitHandler(AccountDocument adoc, String path, Object... o) {

        return new HttpHandler() {

            @Override
            public void handle(HttpExchange exchange) throws IOException {
                ContractAccount a = new ContractAccount();
                ConcreteHandler handler = new ConcreteHandler();
                Object[] result = (Object[]) handler.handlerRequestClient(exchange.getRequestBody(), a, "bank");
                if(result[0] instanceof ContractAccount) {
                    //add the data inside the broker
                    BrokerFactory brokerFactory = new BrokerFactory();
                    Object dataList = brokerFactory.get(path);
                    if(dataList == null) {
                        dataList = new ArrayList<UserAccount>();

                        //find the corresponding user account
                    }

                    Broker broker = new SharedBroker();
                    if(AccountContext.TRANSFER_MODE == o[0].toString()) {
                        try {
                            adoc.transfer((ContractAccount) result[0], path, (Double) result[1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if(AccountContext.CREATE_MODE == o[0].toString()) {
                        adoc.create((ContractAccount) result[0], path, ((ContractAccount) result[0]).getBalance());
                    } else {
                        try {
                            adoc.withdraw((ContractAccount) result[0], path, (Double) result[1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                //provide a render to redirect request

            }
        };

    }

    public void update(ApplicationContext client, ContractAccount a, String... path) throws FileNotFoundException {

        client.notifyMessage(a, path[0], path[1]);

    }

}