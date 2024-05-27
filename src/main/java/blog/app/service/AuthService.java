package blog.app.service;

import blog.app.model.User;
import blog.app.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class AuthService {
    @Autowired
    private AuthRepository authRepository;
    public Optional<User> createUser(User user){
        return Optional.of(authRepository.save(user));
    }
    public String loginUser(String username,String password) {

            User user = authRepository.findByUsername(username);

        return user.getPassword().equals(password) ? user.getId() : "Login failed";

    }
    public Optional<User> getUser(String userId){
        return authRepository.findById(userId);
    }
    public boolean isUserPresent(String userId){
        return authRepository.existsById(userId);
    }

}
