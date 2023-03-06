package com.waza.wazs.broker;

import java.io.FileNotFoundException;

public interface Broker {

    public void operation(String path, String token, Object data) throws FileNotFoundException;
}
