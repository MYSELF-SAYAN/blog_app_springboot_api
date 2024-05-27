package blog.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    @Autowired
    private RedisTemplate redisTemplate;
    public String save(){
        redisTemplate.opsForValue().set("Game", "Chess");
        String game = (String) redisTemplate.opsForValue().get("name");
        System.out.println("Retrieved value from Redis: " + game);
        return game;
    }
}
