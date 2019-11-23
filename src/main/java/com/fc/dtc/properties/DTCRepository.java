package com.fc.dtc.properties;

/**
 * 目前支持字典缓存数据类型
 * @author fangyuan
 */
public enum DTCRepository {
    /**
     * 使用redis作为字典缓存data
     */
    REDIS,

    /**
     * 使用本地内存作为字典data
     *
     */
    LOCAL,

    /**
     * 使用memcache作为字典data
     */
    MEMCACHE
}
