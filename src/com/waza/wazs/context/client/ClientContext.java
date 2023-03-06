package com.waza.wazs.context.client;

import com.waza.wazs.account.ContractAccount;
import com.waza.wazs.account.UserAccount;
import com.waza.wazs.broker.BrokerFactory;
import com.waza.wazs.context.AccountContext;

import java.io.FileNotFoundException;

public interface ClientContext {

    public void inform(AccountContext acc, UserAccount a, String... path) throws FileNotFoundException;

}
