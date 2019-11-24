package com.fc.dtc.lock;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;


/**
 * 使用redis构建分布式锁(使用lun语言)
 * @author fangyuan
 *
 */
public class RedisLock {
    
    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * 尝试获取分布式锁
     * @param redisTemplate Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public static boolean tryGetDistributedLock(RedisTemplate redisTemplate , String lockKey, String requestId, int expireTime) {

        String scriptText = "if redis.call('setNx',KEYS[1],ARGV[1]) == 1 then if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('expire',KEYS[1],ARGV[2]) else return 0 end else return 0 end";

        DefaultRedisScript redisScript = new DefaultRedisScript<>();

        redisScript.setResultType(Long.class);
        redisScript.setScriptText(scriptText);

        Object result = redisTemplate.execute(redisScript, Collections.singletonList(lockKey),requestId,String.valueOf(expireTime));

        if(RELEASE_SUCCESS.equals(result)){
            return true;
        }
        return false;
    }
    

    /**
     * 释放分布式锁
     * @param redisTemplate Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributedLock(RedisTemplate redisTemplate, String lockKey, String requestId) {

        String scriptText = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

        DefaultRedisScript redisScript = new DefaultRedisScript<>();

        redisScript.setResultType(Long.class);
        redisScript.setScriptText(scriptText);


        Object result = redisTemplate.execute(redisScript, Collections.singletonList(lockKey), requestId);

        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;

    }


}
