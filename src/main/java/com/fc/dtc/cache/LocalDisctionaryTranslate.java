package com.fc.dtc.cache;


import com.fc.dtc.constant.CacheConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Getter
public class LocalDisctionaryTranslate  extends AbstractDisctionaryTranslate  {

    public   Map<String, Map<String,String>> localCacheData ;

    public LocalDisctionaryTranslate(JdbcTemplate  jdbcTemplate,DisctionaryJDBCActuator disctionaryJDBCActuator){

        this.localCacheData = new HashMap<>();
        this.jdbcTemplate=jdbcTemplate;
        this.disctionaryJDBCActuator= disctionaryJDBCActuator;
        super.init();
    }

    @Override
    public String get(String cacheKey,String filedKey) {
        return localCacheData.get(cacheKey).get(filedKey);
    }

    @Override
    public void wirteToCache() {

        if (cacheData.size() > 0) {
            // 将代码code保存到缓存中
            this.localCacheData.put(CacheConstant.DMZ_DMMC, cacheData);
        }

        if (cacheData1.size() > 0) {
            this.localCacheData.put(CacheConstant.DMMC_DMZ, cacheData1);
        }

//        if (cacheData2.size() > 0) {
//            this.localCacheData.put(CacheConstant.DEPARTMENT_CODE, cacheData2);
//        }
//
//        if (cacheData3.size() > 0) {
//            this.localCacheData.put(CacheConstant.DMLB_DMMC_DMZ, cacheData3);
//        }

    }

}
