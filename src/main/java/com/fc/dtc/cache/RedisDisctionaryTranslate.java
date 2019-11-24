package com.fc.dtc.cache;

import com.fc.dtc.bean.DisctionaryBean;
import com.fc.dtc.constant.CacheConstant;
import com.fc.dtc.exception.LockExitException;
import com.fc.dtc.lock.RedisLock;
import lombok.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;

/**
 * 使用redis作为底层缓存库
 */
@Getter
@Setter
public class RedisDisctionaryTranslate extends AbstractDisctionaryTranslate {

    private RedisTemplate dtcRedisTemplate;

    private final static int expireTime = 20;

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
    public TreeSet<DisctionaryBean> getDictionaryByType(String type) {

        Object rs = dtcRedisTemplate.opsForHash().get(CacheConstant.DMLB_DMMC_DMZ,type);

        if(rs !=null){
            return (TreeSet) rs;
        }
        return null;
    }

    @Override
    public void dtcRefresh() {

        String lockKey = CacheConstant.LOCK_KEY;

        //随机生成id
        String requestId = UUID.randomUUID().toString();

        //加入分布式锁保证只有一个请求操作
        if(RedisLock.tryGetDistributedLock(dtcRedisTemplate,lockKey,requestId,expireTime)){
            super.init();
            RedisLock.releaseDistributedLock(dtcRedisTemplate,lockKey,requestId);
        }else{
            throw new LockExitException("字典正常重新缓存中请等待。。。。");
        }
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

        if (cacheData3.size() > 0) {
            this.initBaseDataToCache(CacheConstant.DMLB_DMMC_DMZ, cacheData3);
        }

    }

    /**
     * 数据落地redis缓存中
     * @param key
     * @param data
     */
    private void initBaseDataToCache(String key, Map data) {
        // 先删后增
        this.dtcRedisTemplate.delete(key);
        this.dtcRedisTemplate.opsForHash().putAll(key,data);
    }

}
