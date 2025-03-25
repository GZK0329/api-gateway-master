package com.gzk.gateway.core.session;

import com.gzk.gateway.core.bind.Configuration;
import com.gzk.gateway.core.bind.IGenericReference;

public interface GatewaySession {

    /**
     * 执行泛化调用
     * @return
     */
    Object doGet(String methodName, Object parameter);

    Configuration getConfiguration();

    IGenericReference getMapper(String uri);

}
