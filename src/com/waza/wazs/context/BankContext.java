package com.waza.wazs.context;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.waza.wazs.account.AccountDocument;
import com.waza.wazs.account.ContractAccount;
import com.waza.wazs.account.UserAccount;
import com.waza.wazs.broker.Broker;
import com.waza.wazs.broker.BrokerFactory;
import com.waza.wazs.broker.SharedBroker;
import com.waza.wazs.context.client.ClientContext;
import com.waza.wazs.context.handler.ConcreteHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BankContext extends AccountContext {

    private String owner, accountant;

    public String getAccountant() {
        return accountant;
    }

    public void setAccountant(String accountant) {
        this.accountant = accountant;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public HttpHandler submit(AccountDocument adoc, String path, Object... o) {

        return submitHandler(adoc, path, o);

    }

}