package com.fc.dtc.config;


import com.fc.dtc.bean.TranslateProperties;
import com.fc.dtc.cache.DisctionaryJDBCActuator;
import com.fc.dtc.cache.DisctionaryTranslate;
import com.fc.dtc.cache.LocalDisctionaryTranslate;
import com.fc.dtc.cache.RedisDisctionaryTranslate;
import com.fc.dtc.endPoint.RefreshDTCEndpoint;
import com.fc.dtc.properties.DTCProperties;
import com.fc.dtc.utils.DataTransformationUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.ObjectHashMapper;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.core.JdbcTemplate;


import java.util.List;
import java.util.Map;

import static com.fc.dtc.properties.DTCProperties.PREFIX;

/**
 * @
 */
@Configuration
@EnableConfigurationProperties(DTCProperties.class)
@ConditionalOnProperty(prefix = PREFIX, name = "enabled", havingValue = "true")
public class DTCAutoConfigure {

    /**
     *
     */
    public final static String PREFIX = "dtc.cache";


    /**
     * 依赖于spring redis
     */
    @Configuration
    @ConditionalOnClass(RedisTemplate.class)
    @ConditionalOnMissingBean(DisctionaryTranslate.class)
//    @ConditionalOnBean(DisctionaryJDBCActuator.class)
    @ConditionalOnProperty(prefix = DTCAutoConfigure.PREFIX, name = "type", havingValue = "redis")
    public static class RedisConfiguration {

        /**
         * retemplate相关配置
         * @param factory
         * @return
         */
        @Bean("dtcRedisTemplate")
        @Primary
        public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {

            RedisTemplate<String, Object> template = new RedisTemplate<>();

            StringRedisSerializer srs = new StringRedisSerializer();


            // 配置连接工厂
            template.setConnectionFactory(factory);
            template.afterPropertiesSet();

            template.setKeySerializer(srs);
            template.setValueSerializer(srs);
            // 设置hash key 和value序列化模式
            template.setHashKeySerializer(srs);

            template.setHashValueSerializer(new JdkSerializationRedisSerializer());

            return template;
        }

        /**
         *
         * @param dtcRedisTemplate
         * @return
         */
        @Bean("disctionaryTranslate")
        public DisctionaryTranslate redisDisctionaryTranslate(RedisTemplate dtcRedisTemplate, JdbcTemplate jdbcTemplate,DisctionaryJDBCActuator disctionaryJDBCActuator){

            return new RedisDisctionaryTranslate(dtcRedisTemplate,jdbcTemplate,disctionaryJDBCActuator);
        }
    }

    /**
     * 本地local
     */
    @Configuration
    @ConditionalOnMissingBean(DisctionaryTranslate.class)
//    @ConditionalOnBean(DisctionaryJDBCActuator.class)
    @ConditionalOnProperty(prefix = DTCAutoConfigure.PREFIX, name = "type", havingValue = "local")
    public static class LocalConfiguration {

        /**
         *
         * @param jdbcTemplate
         * @param disctionaryJDBCActuator
         * @return
         */
        @Bean("disctionaryTranslate")
        public DisctionaryTranslate localDisctionaryTranslate(JdbcTemplate jdbcTemplate,DisctionaryJDBCActuator disctionaryJDBCActuator){

            return new LocalDisctionaryTranslate(jdbcTemplate,disctionaryJDBCActuator);
        }

    }

    /**
     *
     * @param dsctionaryTranslate
     * @return
     */
    @Bean("dataTransformationUtils")
    public DataTransformationUtils dataTransformationUtils(@Qualifier("translateProperties") Map<String, List<TranslateProperties>> translateProperties, DisctionaryTranslate dsctionaryTranslate){
        return new DataTransformationUtils(translateProperties,dsctionaryTranslate);
    }


    /**
     *
     * @param dsctionaryTranslate
     * @return
     */
    @Bean
    public RefreshDTCEndpoint refreshDTCEndpoint( DisctionaryTranslate dsctionaryTranslate){
        return new RefreshDTCEndpoint(dsctionaryTranslate);
    }

}
