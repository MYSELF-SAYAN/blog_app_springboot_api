package blog.app.controller;

import blog.app.model.LoginRequest;
import blog.app.model.User;
import blog.app.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping ("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("/signup")
    private ResponseEntity<String> createUser(@RequestBody User user){
        try{
            Optional<User> userOptional = authService.createUser(user);
            if(userOptional.isPresent()){
                return ResponseEntity.status(HttpStatus.CREATED).body("User created with username: "+user.getUsername());
            }
            else{
                return ResponseEntity.badRequest().body("User already exists");
            }
        }
        catch (DataIntegrityViolationException e){
            return ResponseEntity.badRequest().body("User already exists");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }


    @PostMapping("/login")
    private ResponseEntity<User> login(@RequestBody LoginRequest loginRequest){
        try{
            String username = authService.loginUser(loginRequest.getUsername(),loginRequest.getPassword());
            Optional<User> userOptional = authService.getUser(username);
            if(userOptional.isPresent()){
                return new ResponseEntity<User>(userOptional.get(), HttpStatus.OK);
            }
            else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
