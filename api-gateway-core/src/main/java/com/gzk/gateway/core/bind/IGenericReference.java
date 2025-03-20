package com.gzk.gateway.core.bind;

/**
 * dubbo中的泛化调用接口
 */
public interface IGenericReference {

    String $invoke(String args);
}
