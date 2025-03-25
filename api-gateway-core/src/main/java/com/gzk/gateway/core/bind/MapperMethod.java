package com.gzk.gateway.core.bind;

import com.gzk.gateway.core.mapping.HttpCommandType;
import com.gzk.gateway.core.session.GatewaySession;

/**
 * @className: MapperMethod
 * @description: TODO
 * @author: gzk
 * @since: 2025/3/21
 **/
public class MapperMethod {

    private final HttpCommandType command;

    private String methodName;

    public MapperMethod(String uri, Configuration configuration) {
        this.command = configuration.getHttpStatement(uri).getHttpCommandType();
        this.methodName = configuration.getHttpStatement(uri).getMethodName();
    }

    public Object execute(GatewaySession session, Object args) {
        Object result = null;
        switch (command) {
            case GET:
                result = session.doGet(methodName, args);
                break;
            case POST:
            case PUT:
            case DELETE:
            case UNKNOWN:
            default:
        }
        return result;
    }


}
