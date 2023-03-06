package com.waza.wazs.context.handler;

import com.sun.net.httpserver.HttpExchange;
import com.waza.wazs.context.AccountContext;

import java.io.FileNotFoundException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public interface Invoker {
    public HttpResponse<String> handleRequest(String url, String path) throws FileNotFoundException;
}
