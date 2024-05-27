package blog.app.repository;

import blog.app.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthRepository extends MongoRepository<User,String> {
    public User findByUsername(String username);
    public User findByEmail(String email);

}
