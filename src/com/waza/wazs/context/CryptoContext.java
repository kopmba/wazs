package com.waza.wazs.context;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.waza.wazs.account.AccountDocument;
import com.waza.wazs.context.client.ClientContext;

import java.io.IOException;

public class CryptoContext extends AccountContext {

    private String owner;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public CryptoContext() {
        super();
    }

    public CryptoContext(String owner) {
        this.owner = owner;
    }

    public HttpHandler submit(AccountDocument adoc, String path, Object... o) {
        return submitHandler(adoc, path, o);
    }

}
