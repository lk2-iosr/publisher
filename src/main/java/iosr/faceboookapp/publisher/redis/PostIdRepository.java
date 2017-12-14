package iosr.faceboookapp.publisher.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * Created by Leszek Placzkiewicz on 13.12.17.
 */
@Repository
public class PostIdRepository {

    private static final String KEY = "PostIds";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private SetOperations<String, String> setOperations;


    @PostConstruct
    private void init() {
        setOperations = redisTemplate.opsForSet();
    }

    public long savePostId(String postId) {
        return setOperations.add(KEY, postId);
    }

    public Set<String> findAllPostIds() {
        return setOperations.members(KEY);
    }

    public boolean isMember(String postId) {
        return setOperations.isMember(KEY, postId);
    }

    @Configuration
    public static class Config {

        @Value("${spring.redis.host}")
        private String redisHost;

        @Value("${spring.redis.port}")
        private String redisPort;


        @Bean
        JedisConnectionFactory jedisConnectionFactory() {
            JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
            jedisConFactory.setHostName(redisHost);
            jedisConFactory.setPort(Integer.valueOf(redisPort));
            return jedisConFactory;
        }

        @Bean
        public RedisTemplate<String, String> redisTemplate() {
            RedisTemplate<String, String> template = new RedisTemplate<>();
            template.setConnectionFactory(jedisConnectionFactory());
            return template;
        }
    }
}
