package com.waza.wazs.context.client;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.waza.wazs.account.AccountDocument;
import com.waza.wazs.account.ContractAccount;
import com.waza.wazs.account.UserAccount;
import com.waza.wazs.broker.BrokerFactory;
import com.waza.wazs.broker.SharedBroker;
import com.waza.wazs.broker.UnsharedBroker;
import com.waza.wazs.context.AccountContext;
import com.waza.wazs.context.handler.ConcreteHandler;
import com.waza.wazs.context.handler.Invoker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ApplicationContext implements ClientContext {



    @Override
    public void inform(AccountContext acc, UserAccount a, String... path) throws FileNotFoundException {

        acc.notifyMessage(a, path[0], path[1]);

    }

    public void notifyMessage(ContractAccount a, String... path) throws FileNotFoundException {
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
                   for(ContractAccount ca : ((UserAccount) account).getAccounts()) {
                       if(ca.getUsername() == a.getUsername() || ca.getPhone() == a.getPhone()) {
                           String message = Instant.now() + "Mise à jour automatique de compte utilisateur " + a.getUsername() + "concernant le contrat utilisateur " + ca.getUsername() + ". Le nouveau solde est de " + ca.getBalance();
                           if(result instanceof List) {
                               ((List) result).add(message);
                           }

                       }
                   }
               }
            }
        }

        sb.operation(path[0], null, result);
    }
    public List sharingState(SharedBroker broker, String path, Object data) throws FileNotFoundException {
        //get the data from the broker factory to share with the any context
        broker.operation(path, null, data);
        BrokerFactory bf = new BrokerFactory();
        Object result = bf.get(path);
        if(result instanceof List) {
            return (List) result;
        }
        return null;
    }

    public HttpHandler getShared(String url, String... path) {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                ConcreteHandler handler = new ConcreteHandler<>();
                HttpResponse<String> response = handler.handleRequest(url, path[0]);
                BrokerFactory brokerFactory = new BrokerFactory();
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
        };
    }
}
