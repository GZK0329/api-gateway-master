package com.gzk.gateway.core.session;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @className: GenericReferenceSessionFactory
 * @description: TODO
 * @author: gzk
 * @since: 2025/3/20
 **/
@Slf4j
public class GenericReferenceSessionFactory implements IGenericReferenceSessionFactory {

    private final Configuration configuration;

    public GenericReferenceSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Future<Channel> openSession() throws ExecutionException, InterruptedException {
        SessionServer sessionServer = new SessionServer(configuration);
        Future<Channel> future = Executors.newFixedThreadPool(2).submit(sessionServer);
        Channel channel = future.get();
        if (channel == null) {
            throw new RuntimeException("Channel is null");
        }

        while (!channel.isActive()) {
            log.info("Session is init, please wait...");
        }

        log.info("Session is active");
        return future;
    }
}
