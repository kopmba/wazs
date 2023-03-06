package com.waza.wazs.broker;

import java.io.FileNotFoundException;

public class UnsharedBroker implements Broker {
    @Override
    public void operation(String path, String token, Object data) throws FileNotFoundException {
        //we compare session value with the token if exists put the data
        BrokerFactory brokerFactory = new BrokerFactory();
        Object result = brokerFactory.get("session");
        if(result.toString() == token) {
            //set or get the resource
            brokerFactory.set(path, data);
        }
    }
}
