package blog.app.service;

import blog.app.model.Post;
import blog.app.model.PostCreationRequest;
import blog.app.model.User;
import blog.app.repository.AuthRepository;
import blog.app.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private AuthRepository authRepository;

    public Optional<String> createPost(PostCreationRequest postCreationRequest){
        Post post = new Post();
        post.setTitle(postCreationRequest.getTitle());
        post.setContent(postCreationRequest.getContent());
        post.setAuthorId(postCreationRequest.getAuthorId());
        post.setImageUrl(postCreationRequest.getImageUrl());
        post.setTags(postCreationRequest.getTags());
        Post savedpost=postRepository.save(post);
        return Optional.of(savedpost.getId());
    }
    public void addPostToUser(String postId, String userId){
        User user = authRepository.findById(userId).orElse(null);
        if(user != null){
            if(user.getPosts() == null){
                user.setPosts(new ArrayList<>());
            }
            user.getPosts().add(postRepository.findById(postId).orElse(null));
            authRepository.save(user);
        }
    }

    public void deletePost(String postId){
        postRepository.deleteById(postId);
    }
    public void deletePostFromUser(String postId, String userId){
        User user = authRepository.findById(userId).orElse(null);
        if(user != null){
            if(user.getPosts() != null){
                user.getPosts().removeIf(p -> p.getId().equals(postId));
                authRepository.save(user);
            }
        }
    }




    public Optional<Post> getPost(String postId){
        return postRepository.findById(postId);
    }


    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }
    public List<Post> getPostByUser(String userId){
        return authRepository.findById(userId).map(User::getPosts).orElse(null);
    }
}
