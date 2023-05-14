package com.list.springredis;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.transaction.Transactional;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional

public class RedisTemplateTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

//    @AfterEach
//    void after() {
//
//        redisTemplate.delete(key);
//    }

    @Test
    @DisplayName("opsForValue test")
    public void valueTest() {

        String key = "value";

        redisTemplate.opsForValue().set(key, "setValue");

        String setKey = redisTemplate.opsForValue().get(key);

        assertThat(setKey).isEqualTo("setValue");
    }

    @Test
    @DisplayName("opsForList test")
    public void listTest() {

        String key = "list";

        redisTemplate.opsForList().rightPush(key, "1stList");
        redisTemplate.opsForList().rightPush(key, "2ndList");
        redisTemplate.opsForList().rightPush(key, "3rdList");
        redisTemplate.opsForList().rightPush(key, "4thList");

        Long size = redisTemplate.opsForList().size(key);
        assertThat(size).isEqualTo(4);

        List<String> range1 = redisTemplate.opsForList().range(key, 0, size);
        assertThat(range1).containsExactly("1stList", "2ndList", "3rdList", "4thList");

        String s = redisTemplate.opsForList().rightPop(key);
        assertThat(s).isEqualTo("4thList");

        List<String> leftPop = redisTemplate.opsForList().leftPop(key, 2);
        assertThat(leftPop).containsExactly("1stList", "2ndList");

        List<String> rightPop = redisTemplate.opsForList().rightPop(key, 2);
        assertThat(rightPop).containsExactly("3rdList");

        List<String> range = redisTemplate.opsForList().range(key, 0, size);
        Assertions.assertThat(range).isEmpty();
    }


    @Test
    @DisplayName("opsForSet test")
    public void setTest() {
        String key = "set";

        redisTemplate.opsForSet().add(key, "1stSet");
        redisTemplate.opsForSet().add(key, "1stSet");
        redisTemplate.opsForSet().add(key, "2ndSet");

        Long size = redisTemplate.opsForSet().size(key);
        List<String> pop = redisTemplate.opsForSet().pop(key, size);

        assertThat(pop).containsExactly("1stSet", "2ndSet");
    }

    @Test
    @DisplayName("opsForHash test")
    public void hashTest() {
        String key = "hash";

        redisTemplate.opsForHash().put(key, "1HashKey", "1stHash");
        redisTemplate.opsForHash().put(key, "2HashKey", "2ndHash");
        redisTemplate.opsForHash().put(key, "3HashKey", "3rdHash");
        redisTemplate.opsForHash().put(key, "4HashKey", "4thHash");

        Object hash1 = redisTemplate.opsForHash().get(key, "1HashKey");
        assertThat(hash1).isEqualTo("1stHash");

        Object hash2 = redisTemplate.opsForHash().get(key, "wrong2HashKey");
        assertThat(hash2).isNotEqualTo("2ndHash");

        Object hash3 = redisTemplate.opsForHash().get(key, "3HashKey");
        assertThat(hash3).isEqualTo("3rdHash");
    }

    @Test
    @DisplayName("expire test")
    public void expireTest() throws InterruptedException {

        String key = "expire";

        redisTemplate.opsForHash().put(key, "1ExpireKey", "1stExpire");
        redisTemplate.opsForHash().put(key, "2ExpireKey", "2ndExpire");
        redisTemplate.opsForHash().put(key, "3ExpireKey", "3rdExpire");
        redisTemplate.opsForHash().put(key, "4ExpireKey", "4thExpire");

        //3초 후에 key 가 만료되도록 설정
        redisTemplate.expire(key, Duration.ofSeconds(3));

        Object beforeExpired = redisTemplate.opsForHash().get(key, "2ExpireKey");
        assertThat(beforeExpired).isEqualTo("2ndExpire");

        //3초간 SLEEP
        Thread.sleep(3000);

        Object afterExpired = redisTemplate.opsForHash().get(key, "2ExpireKey");
        assertThat(afterExpired).isNotEqualTo("2ndExpire");
    }
}
