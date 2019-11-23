package com.fc.dtc.cache;


/**
 * 字典翻译抽象
 * @author fangyuan
 */
public  interface DisctionaryTranslate {


    /**
     * 获取key-value
     */
    public String get(String cacheKey,String filedKey);

    /**
     *
     */
    public void wirteToCache();


}
