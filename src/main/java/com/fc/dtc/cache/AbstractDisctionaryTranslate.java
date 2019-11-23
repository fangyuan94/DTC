package com.fc.dtc.cache;

import com.fc.dtc.bean.DisctionaryBean;
import com.fc.dtc.constant.CacheConstant;
import com.fc.dtc.exception.TranslateException;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 *
 */
abstract class AbstractDisctionaryTranslate  implements DisctionaryTranslate {


    protected DisctionaryJDBCActuator disctionaryJDBCActuator;


    protected JdbcTemplate jdbcTemplate;

    // (dmlb_dmzm,dmmc)
    protected Map<String, String> cacheData = new HashMap<String, String>(200);

    // //(dmlb_dmmc,dmz)
    protected Map<String, String> cacheData1 = new HashMap<String, String>(200);

    // (dmlb,(dmz,dmmc))
//    protected Map<String, Map<String, String>> cacheData3 = new HashMap<String, Map<String, String>>(200);


    @Override
    public String get(String cacheKey,String filedKey) {
       throw new TranslateException("需实现get方法");
    }

    @Override
    public void wirteToCache() {

        throw new TranslateException("需实现init方法");

    }
    
    public final void init(){

        //将数据写入缓存中
        List<DisctionaryBean> dbs = this.disctionaryJDBCActuator.execute(this.jdbcTemplate);

        dbs.forEach(disctionaryBean->{
            String type = disctionaryBean.getType();
            String code = disctionaryBean.getCode();
            String name = disctionaryBean.getName();
            cacheData.put(type + CacheConstant.SEPARATOR + code, name);
            cacheData1.put(type + CacheConstant.SEPARATOR + name, code);

//            Map<String, String> v;
//            if (!cacheData3.containsKey(type)) {
//                v = new HashMap<String, String>();
//            } else {
//                v = cacheData3.get(type);
//            }
//            v.put(code, name);
//            v.put(name, code);
//            cacheData3.put(type, v);
        });

        wirteToCache();
    }
}
