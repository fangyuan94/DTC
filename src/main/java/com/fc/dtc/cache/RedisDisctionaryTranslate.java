package com.fc.dtc.cache;

import com.fc.dtc.constant.CacheConstant;
import lombok.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

/**
 * redis  连接
 */
@Getter
@Setter
public class RedisDisctionaryTranslate extends AbstractDisctionaryTranslate {


    private RedisTemplate dtcRedisTemplate;


    public RedisDisctionaryTranslate(RedisTemplate dtcRedisTemplate, JdbcTemplate jdbcTemplate,DisctionaryJDBCActuator disctionaryJDBCActuator){

        this.dtcRedisTemplate = dtcRedisTemplate;
        this.jdbcTemplate=jdbcTemplate;
        this.disctionaryJDBCActuator= disctionaryJDBCActuator;
        super.init();
    }


    @Override
    public String get(String cacheKey,String filedKey) {

        Object rs = dtcRedisTemplate.opsForHash().get(cacheKey,filedKey);

        if(rs !=null){
            return (String) rs;
        }
        return null;
    }


    @Override
    public void wirteToCache() {

        if (cacheData.size() > 0) {
            // 将代码code保存到缓存中
            this.initBaseDataToCache(CacheConstant.DMZ_DMMC, cacheData);
        }

        if (cacheData1.size() > 0) {
            this.initBaseDataToCache(CacheConstant.DMMC_DMZ, cacheData1);
        }

//        if (cacheData2.size() > 0) {
//            this.initBaseDataToCache(CacheConstant.DEPARTMENT_CODE, cacheData2);
//        }

//        if (cacheData3.size() > 0) {
//            this.initBaseDataSerToCache(CacheConstant.DMLB_DMMC_DMZ, cacheData3);
//        }

    }


    private void initBaseDataToCache(String key, Map<String, String> data) {
        // 先删后增
        this.dtcRedisTemplate.delete(key);
        this.dtcRedisTemplate.opsForHash().putAll(key,data);
    }

//    private void initBaseDataSerToCache(String key,
//                                        Map<String, Map<String, String>> data) {
//        // 先删后增
//        this.dtcRedisTemplate.delete(key);
//
//        this.dtcRedisTemplate.opsForHash().putAll(key,data);
//    }

}
