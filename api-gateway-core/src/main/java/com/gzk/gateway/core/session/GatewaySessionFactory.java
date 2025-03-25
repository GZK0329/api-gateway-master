package com.gzk.gateway.core.session;

import java.util.concurrent.ExecutionException;

/**
 * 泛化调用会话接口
 */
public interface GatewaySessionFactory {
    GatewaySession openSession(String uri) throws ExecutionException, InterruptedException;
}
