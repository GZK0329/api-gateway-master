package com.gzk.apigatewaytest.interfaces;

import com.alibaba.fastjson.JSON;
import com.gzk.apigatewaytest.rpc.IApiGatewayInterface;
import com.gzk.apigatewaytest.rpc.dto.XReq;
import org.apache.dubbo.config.annotation.Service;

/**
 * @className: ApiGatewayInterface
 * @description: TODO
 * @author: gzk
 * @since: 2025/3/28
 **/
@Service(version = "1.0.0")
public class ApiGatewayInterface implements IApiGatewayInterface {
    @Override
    public String sayHi(String str) {
        return "say hi by api gateway interface";
    }

    @Override
    public String insert(XReq req) {
        return "insert by api gateway interface, request body: " + JSON.toJSONString(req);
    }

    @Override
    public String test(String str, XReq req) {
        return "test by api gateway interface, param1: " + str + ",request body: " + JSON.toJSONString(req);
    }
}
