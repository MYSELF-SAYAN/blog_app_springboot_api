package blog.app.controller;

import blog.app.model.Post;
import blog.app.model.PostCreationRequest;
import blog.app.service.AuthService;
import blog.app.service.PostService;
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
    @PostMapping ("/{parentId}")

    // Create a new post
    public ResponseEntity<String> createPost(@RequestBody PostCreationRequest postCreationRequest){
        try{
            Optional<String> response = postService.createPost(postCreationRequest);
            if(response.isPresent()){
                postService.addPostToUser(response.get(), postCreationRequest.getAuthorId());
                return new ResponseEntity<String>("Post created successfully", HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<String>("Error creating post", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        catch (Exception e){
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
            return new ResponseEntity<List<Post>>(postService.getAllPosts(), HttpStatus.OK);
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
                return new ResponseEntity<List<Post>>(postService.getPostByUser(userId), HttpStatus.OK);
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
