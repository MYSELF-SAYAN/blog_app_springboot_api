package blog.app.controller;

import blog.app.model.Post;
import blog.app.model.PostCreationRequest;
import blog.app.service.AuthService;
import blog.app.service.ImageUploadImpl;
import blog.app.service.PostService;
import blog.app.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private AuthService authService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ImageUploadImpl imageUpload;

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @PostMapping(value = "/{parentId}")
// Create a new post
    public ResponseEntity<String> createPost(@RequestBody PostCreationRequest postCreationRequest){
        try{
            Optional<String> response = postService.createPost(postCreationRequest);
            if(response.isPresent()){
                postService.addPostToUser(response.get(), postCreationRequest.getAuthorId());
                return new ResponseEntity<String>(response.get(), HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<String>("Error creating post", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        catch (Exception e){
            logger.error("Error creating post: "+e.getMessage());
            return new ResponseEntity<String>("Error creating post", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

// Delete a post
    @DeleteMapping("/{postId}/{userId}")
    public ResponseEntity<String> deletePost(@PathVariable String postId, @PathVariable String userId){
       try{
              if(authService.isUserPresent(userId)){
                postService.deletePostFromUser(postId, userId);
                postService.deletePost(postId);
                return new ResponseEntity<String>("Post deleted successfully", HttpStatus.OK);
              }
              else{
                return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
              }
       }
       catch (Exception e){
           return new ResponseEntity<String>("", HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

// Get a post by its id
    @GetMapping ("/user/{postId}")
    public ResponseEntity<Post> getPost (@PathVariable String postId){
        try{
           Optional <Post> post = postService.getPost(postId);
              if(post.isPresent()){
                return new ResponseEntity<Post>(post.get(), HttpStatus.OK);
              }
              else{
                return new ResponseEntity<Post>(HttpStatus.NOT_FOUND);
              }
        }
        catch (Exception e){
            return new ResponseEntity<Post>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//Get all posts in database
    @GetMapping("/")
    public ResponseEntity<List<Post>> getAllPosts(){
        try{
            List<Post> cachedPost= redisService.getAllPostsFromCache("allPosts");
            if(cachedPost != null){
                return new ResponseEntity<List<Post>>(cachedPost, HttpStatus.OK);
            }
            else{
                List<Post> posts = postService.getAllPosts();
                redisService.setAllPostsToCache("allPosts", posts, 600);
                logger.info("Posts fetched from database");
                return  new ResponseEntity<List<Post>>(posts, HttpStatus.OK);
            }
       }
        catch (Exception e){
            return new ResponseEntity<List<Post>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

// Get all posts by a user
    @GetMapping ("/{userId}")
    public ResponseEntity<List<Post>>  getPostsByUser (@PathVariable String userId){
       try{
              if(authService.isUserPresent(userId)){
                  List<Post> cachedPost= redisService.getAllPostsFromCache("user"+userId);
                  if(cachedPost != null){
                      logger.info("Posts fetched from cache");
                      return new ResponseEntity<List<Post>>(cachedPost, HttpStatus.OK);
                  }
                  else{
                      List<Post> posts = postService.getPostByUser(userId);
                      redisService.setAllPostsToCache("user"+userId, posts, 20);
                      logger.info("Posts fetched from database");
                      return  new ResponseEntity<List<Post>>(posts, HttpStatus.OK);
                  }

              }
              else{
                return new ResponseEntity<List<Post>>(HttpStatus.NOT_FOUND);
              }
         }
         catch (Exception e){
              return new ResponseEntity<List<Post>>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }
}
