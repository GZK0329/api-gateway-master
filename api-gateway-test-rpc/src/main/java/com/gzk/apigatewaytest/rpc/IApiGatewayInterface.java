package com.gzk.apigatewaytest.rpc;


import com.gzk.apigatewaytest.rpc.dto.XReq;

public interface IApiGatewayInterface {

    String sayHi(String str);

    String insert(XReq req);

    String test(String str, XReq req);

}
