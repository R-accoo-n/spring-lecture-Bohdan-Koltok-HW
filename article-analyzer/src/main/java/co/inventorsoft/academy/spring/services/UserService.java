package co.inventorsoft.academy.spring.services;

import co.inventorsoft.academy.spring.models.NotificationType;
import co.inventorsoft.academy.spring.models.User;
import co.inventorsoft.academy.spring.repositories.UserJsonRepository;
import co.inventorsoft.academy.spring.repositories.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * User service to operate user repository and make further actions.
 */

@Service
public class UserService {
    private final UserJsonRepository userJsonRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserJsonRepository userJsonRepository, UserRepository userRepository) {
        this.userJsonRepository = userJsonRepository;
        this.userRepository = userRepository;
    }

    public Set<User> getAllUsersFromJson(){
        return userJsonRepository.getAllUsers();
    }

    public User getUserByIdFromJson(Long userId){
        return userJsonRepository.getUserById(userId);
    }

    public User getUserById(Long userId){
        return userRepository.findById(userId).orElseGet(() -> User.builder()
            .email("userNotFound@mail.com")
            .build()
        );
    }

    public User getUserByEmail(String userEmail){
        return userRepository.findByEmail(userEmail).orElseGet(() -> User.builder()
            .email("userNotFound@mail.com")
            .build()
        );
    }

    public User createUser(User user){

        return userRepository.save(user);
    }

    public User updateUser(Long id, User newUser){
        User updatedUser = getUserById(id);
        updatedUser.setEmail(newUser.getEmail());
        updatedUser.setSlackId(newUser.getSlackId());
        updatedUser.setUsername(newUser.getUsername());
        updatedUser.setNotificationType(newUser.getNotificationType());
        updatedUser.setId(newUser.getId());

        return userRepository.save(updatedUser);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void deleteUserById(Long userId){
        userRepository.deleteById(userId);
    }




}
