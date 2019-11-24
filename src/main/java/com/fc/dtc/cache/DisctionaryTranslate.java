package com.fc.dtc.cache;


import com.fc.dtc.bean.DisctionaryBean;

import java.util.TreeSet;


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

    /**
     * 根据字典类型获取
     * @param type
     * @return
     */
    public TreeSet<DisctionaryBean> getDictionaryByType(String type);

    /**
     * 刷新整个缓存
     */
    public void dtcRefresh();

//    /**
//     * 刷新某些类型缓存
//     * @param type
//     */
//    public void dtcRefresh(String type);
}
