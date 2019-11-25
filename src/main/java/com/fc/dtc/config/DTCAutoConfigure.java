package com.fc.dtc.config;


import com.fc.dtc.bean.TranslateProperties;
import com.fc.dtc.cache.DictionaryJDBCActuator;
import com.fc.dtc.cache.DictionaryTranslate;
import com.fc.dtc.cache.LocalDictionaryTranslate;
import com.fc.dtc.cache.RedisDictionaryTranslate;
import com.fc.dtc.endPoint.RefreshDTCEndpoint;
import com.fc.dtc.properties.DTCProperties;
import com.fc.dtc.utils.DataTransformationUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
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
    @ConditionalOnMissingBean(DictionaryTranslate.class)
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
        public DictionaryTranslate redisDisctionaryTranslate(RedisTemplate dtcRedisTemplate, JdbcTemplate jdbcTemplate, DictionaryJDBCActuator dictionaryJDBCActuator){

            return new RedisDictionaryTranslate(dtcRedisTemplate,jdbcTemplate, dictionaryJDBCActuator);
        }
    }

    /**
     * 本地local
     */
    @Configuration
    @ConditionalOnMissingBean(DictionaryTranslate.class)
//    @ConditionalOnBean(DisctionaryJDBCActuator.class)
    @ConditionalOnProperty(prefix = DTCAutoConfigure.PREFIX, name = "type", havingValue = "local")
    public static class LocalConfiguration {

        /**
         *
         * @param jdbcTemplate
         * @param dictionaryJDBCActuator
         * @return
         */
        @Bean("disctionaryTranslate")
        public DictionaryTranslate localDisctionaryTranslate(JdbcTemplate jdbcTemplate, DictionaryJDBCActuator dictionaryJDBCActuator){

            return new LocalDictionaryTranslate(jdbcTemplate, dictionaryJDBCActuator);
        }

    }

    /**
     *
     * @param dsctionaryTranslate
     * @return
     */
    @Bean("dataTransformationUtils")
    public DataTransformationUtils dataTransformationUtils(@Qualifier("translateProperties") Map<String, List<TranslateProperties>> translateProperties, DictionaryTranslate dsctionaryTranslate){
        return new DataTransformationUtils(translateProperties,dsctionaryTranslate);
    }


    /**
     *
     * @param dsctionaryTranslate
     * @return
     */
    @Bean
    public RefreshDTCEndpoint refreshDTCEndpoint( DictionaryTranslate dsctionaryTranslate){
        return new RefreshDTCEndpoint(dsctionaryTranslate);
    }

}
