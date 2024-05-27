package blog.app.repository;

import blog.app.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends MongoRepository<Post,String>{
    public Optional<Post> findById(String id);
}
