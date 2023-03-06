package com.waza.wazs.broker;

import com.sun.net.httpserver.Authenticator;

import java.io.FileNotFoundException;

public class SharedBroker implements Broker {
    @Override
    public void operation(String path, String token, Object data) throws FileNotFoundException {
        BrokerFactory brokerFactory = new BrokerFactory();
        brokerFactory.set(path, data);
    }


    public void operation(String path, String token, Object data, int mode) throws FileNotFoundException {
        if(mode != 1 && token != null) {
            operation(path, token, data);
        }
    }
}
