package com.gzk.gateway.core.mapping;

/**
 * @className: HttpStatement
 * @description: TODO
 * @author: gzk
 * @since: 2025/3/21
 **/
public class HttpStatement {
    /**
     * 应用名称
     */
    private String applicationName;
    /**
     * 服务接口；RPC、其他
     */
    private String interfaceName;
    /**
     * 服务方法；RPC#method
     */
    private String methodName;
    /**
     * 网关接口
     */
    private String uri;
    /**
     * 接口类型；GET、POST、PUT、DELETE
     */
    private HttpCommandType httpCommandType;

    public HttpStatement(String applicationName, String interfaceName, String methodName, String uri, HttpCommandType httpCommandType) {
        this.applicationName = applicationName;
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.uri = uri;
        this.httpCommandType = httpCommandType;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getUri() {
        return uri;
    }

    public HttpCommandType getHttpCommandType() {
        return httpCommandType;
    }
}
