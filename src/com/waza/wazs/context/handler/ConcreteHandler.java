package com.waza.wazs.context.handler;

import com.sun.net.httpserver.HttpExchange;
import com.waza.wazs.account.ContractAccount;
import com.waza.wazs.account.UserAccount;
import com.waza.wazs.broker.BrokerFactory;
import com.waza.wazs.context.AccountContext;
import com.waza.wazs.context.BankContext;
import com.waza.wazs.context.CryptoContext;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConcreteHandler<T> implements Invoker {
    @Override
    public HttpResponse<String> handleRequest(String url, String path) throws FileNotFoundException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/xml")
                .POST(HttpRequest.BodyPublishers.ofFile(Paths.get(path)))
                .build();

        HttpResponse<String> result = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();

        return result;
    }

    public void httpResponse(HttpExchange t, byte[] data) throws IOException {

        InputStream is = t.getRequestBody();
        t.sendResponseHeaders(200, data.length);
        OutputStream os = t.getResponseBody();
        os.write(data);
        os.close();
    }

    public Object handlerRequestClient(InputStream stream, ContractAccount ca, String accountType) throws IOException {
        //Read the HTTP request from the client socket
        InputStreamReader isr = new InputStreamReader(stream);
        BufferedReader in = new BufferedReader(isr);
        String inputLine = in.readLine();
        String strValues = inputLine.split(" ")[0];

        ca.setFirstname(getAttributeValue(inputLine, "firstname"));
        ca.setLastname(getAttributeValue(inputLine, "lastname"));
        ca.setCity(getAttributeValue(inputLine, "city"));
        ca.setCountry(getAttributeValue(inputLine, "country"));
        ca.setContractRef(getAttributeValue(inputLine, "contractRef"));
        ca.setPhone(getAttributeValue(inputLine, "phone"));
        ca.setEmail(getAttributeValue(inputLine, "email"));
        ca.setBalance(Double.valueOf(getAttributeValue(inputLine, "balance")));
        ca.setPassword(getAttributeValue(inputLine, "password"));
        ca.setToken(getAttributeValue(inputLine, "token"));
        ca.setUsername(getAttributeValue(inputLine, "username"));

        AccountContext ac = null;
        if(accountType == "crypto") {
            ac = new CryptoContext();
            ac.setOwner(getAttributeValue(inputLine, "owner"));
        } else {
            ac = new BankContext();
            ac.setOwner(getAttributeValue(inputLine, "owner"));
            ac.setAccountant(getAttributeValue(inputLine, "accountant"));
        }

        ca.setAccountContext(ac);

        Object[] result = {ca, getAttributeValue(inputLine, "amount")};
        return result;

    }

    public String getAttributeValue(String inputString, String attribute) {
        String result = "";
        if(inputString.contains(attribute)) {

            int indexA = inputString.indexOf(attribute);
            String valueA = inputString.substring(indexA+1);

            int indexV = valueA.indexOf('&');
            result += valueA.substring(0, indexV);

        }
        return result;
    }

    public double balance(ContractAccount a, String... options) throws FileNotFoundException {
        HttpResponse<String> result = handleRequest(options[0], options[1]);

        BrokerFactory bf = new BrokerFactory();
        //we have to delete to make sure we create another
        bf.set(options[2], result.body());

        Object list = bf.get(options[2]);

        if(list instanceof List) {
            for(Object account : (List) list) {
                if(account instanceof UserAccount) {
                    for(ContractAccount ca : ((ContractAccount) account).getAccounts()) {
                        if(ca.getUsername() == a.getUsername() || ca.getPhone() == a.getPhone()) {
                            return a.getBalance();
                        }
                    }
                }
            }
        }
        return 0;
    }

    public String getBalance(ContractAccount a, String... options) throws FileNotFoundException {
        double balance = balance(a, options);
        String message = "Your balance in your account is : " + balance;
        return message;
    }

    public void withdraw(ContractAccount a, double amount, String... options) throws FileNotFoundException {


        HttpResponse<String> result = handleRequest(options[0], options[1]);

        BrokerFactory bf = new BrokerFactory();
        bf.set(options[2], result.body());

        Object list = bf.get(options[2]);

        Object sharedList = bf.get(options[3]);
        //file must contains the account username
        Object unsharedList = bf.get(options[4]);

        if(sharedList == null) {
            sharedList = new ArrayList<String>();
        }

        if(unsharedList == null) {
            unsharedList = new ArrayList<String>();
        }

        if(list instanceof List) {
            for(Object account : (List) list) {
                if(account instanceof UserAccount) {
                    for(ContractAccount ca : ((ContractAccount) account).getAccounts()) {
                        if(ca.getUsername() == a.getUsername() || ca.getPhone() == a.getPhone()) {
                            a.setBalance(a.getBalance() - amount);
                            String sharedessage = Instant.now() + " : " + a.getUsername() + " made a withdraw of " + amount + " " + options[5] + ", his new balance is " + a.getBalance();
                            String unsharedMessage = Instant.now() + " : Withdraw -> " + amount + ", Balance -> " + a.getBalance();
                            if(sharedList instanceof List) {
                                ((List) sharedList).add(unsharedMessage);
                            }

                            if(unsharedList instanceof List) {
                                ((List) unsharedList).add(unsharedMessage);
                            }
                        }
                    }
                }
            }
        }

        //we have to create sharing resource to inform the accountcontext
        bf.set(options[3], sharedList);

        //we create the unsharing resource specific to the clientcontext
        bf.set(options[4], unsharedList);



    }
}
