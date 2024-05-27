package blog.app.service;

import blog.app.model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    @Autowired
    private RedisTemplate redisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);

    public void setAllPostsToCache(String key, List<Post> posts,int seconds){
        try{
            redisTemplate.opsForValue().set(key, posts, seconds, TimeUnit.SECONDS);
        }
        catch(Exception e){
            logger.error("Error in setting posts to cache: "+e.getMessage());
        }
    }

    public List<Post> getAllPostsFromCache(String key){
        try{
            List<Post> posts = (List<Post>) redisTemplate.opsForValue().get(key);
            logger.info("Posts availavle in cache ");
            return posts;
        }
        catch(Exception e){
            logger.error("Error in getting posts from cache: "+e.getMessage());
            return null;
        }
    }

}
