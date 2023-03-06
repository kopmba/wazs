package com.waza.wazs.context.client;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.waza.wazs.account.UserAccount;
import com.waza.wazs.broker.BrokerFactory;
import com.waza.wazs.broker.UnsharedBroker;
import com.waza.wazs.context.AccountContext;
import com.waza.wazs.context.handler.ConcreteHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class UserAccountContext implements ClientContext {
    @Override
    public void inform(AccountContext acc, UserAccount a, String... path) throws FileNotFoundException {
        //crypto mode
        acc.notifyMessage(a, path[0], path[1]);
    }

    public List unsharingState(UnsharedBroker broker, String path, String token, Object data) throws FileNotFoundException {
        //get the data from the broker factory to use only by authentication from user
        broker.operation(path, token, data);
        BrokerFactory bf = new BrokerFactory();
        Object result = bf.get(path);
        if(result instanceof List) {
            return (List) result;
        }
        return null;
    }

    public HttpHandler get(String url, String token, String... path) {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {

                ConcreteHandler handler = new ConcreteHandler<>();
                HttpResponse<String> response = handler.handleRequest(url, path[0]);
                BrokerFactory brokerFactory = new BrokerFactory();
                Object session = brokerFactory.get("session");

                if(session.toString() == token) {
                    Object result = brokerFactory.get(response.toString().getBytes());
                    if(result != null) {
                        result = new ArrayList();
                    }

                    StringBuilder messageBuilder = new StringBuilder();
                    if(result instanceof List) {
                        for(Object message : (List) result) {
                            messageBuilder.append(message + "\n");
                        }
                    }

                    brokerFactory.set(path[1], result);
                    handler.httpResponse(exchange, messageBuilder.toString().getBytes());
                }
            }
        };
    }
}
