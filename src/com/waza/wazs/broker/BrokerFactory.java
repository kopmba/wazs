package com.waza.wazs.broker;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

public class BrokerFactory {

    public Object get(String path) throws FileNotFoundException {
        //call the broker factory to get the data from serialization
        XMLDecoder d = new XMLDecoder(new BufferedInputStream(new FileInputStream(path)));
        Object result = d.readObject();
        d.close();

        return result;
    }

    public Object get(byte[] data) throws FileNotFoundException {
        //call the broker factory to get the data from serialization
        XMLDecoder d = new XMLDecoder(new BufferedInputStream(new ByteArrayInputStream(data)));
        Object result = d.readObject();
        d.close();

        return result;
    }


    public void set(String path, Object value) throws FileNotFoundException {
        XMLEncoder e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(path)));
        e.writeObject(value);
        e.close();
    }

}
