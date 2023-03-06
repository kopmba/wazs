package com.waza.wazs.context;

import com.sun.net.httpserver.HttpExchange;

import java.io.*;

public class Util {



    public static String readFile(String path) throws IOException {
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);

        //Use the buffered reader
        BufferedReader in = new BufferedReader(new InputStreamReader(fis));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        } in.close();
        //System.out.println(response.toString());
        return response.toString();

    }

}
