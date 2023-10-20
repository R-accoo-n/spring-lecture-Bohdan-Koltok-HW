package co.inventorsoft.academy.spring.services;

import co.inventorsoft.academy.spring.exceptions.NotFoundException;
import co.inventorsoft.academy.spring.exceptions.ValidationException;
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
        Optional<User> currentUser = userRepository.findById(userId);

        if(currentUser.isEmpty()){
            throw new NotFoundException(NotFoundException.USER_ID_NOT_FOUND);
        }

        return currentUser.get();
    }

    public User getUserByEmail(String userEmail){
        Optional<User> currentUser = userRepository.findByEmail(userEmail);

        if(currentUser.isEmpty()){
            throw new NotFoundException(NotFoundException.USER_EMAIL_NOT_FOUND);
        }

        return currentUser.get();
    }

    public User createUser(User user){
        if(userRepository.findById(user.getId()).isPresent()){
            throw new ValidationException(ValidationException.USER_ID_EXISTS);
        }

        if(checkUser(user)){
            throw new ValidationException(ValidationException.USER_MUST_NOT_HAVE_NULL_FIELDS);
        }

        return userRepository.save(user);
    }

    public User updateUser(Long id, User newUser){
        if(checkUser(newUser)){
            throw new ValidationException(ValidationException.USER_MUST_NOT_HAVE_NULL_FIELDS);
        }

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
        if(userRepository.findById(userId).isEmpty()){
            throw new ValidationException(ValidationException.USER_ID_DOES_NOT_EXIST);
        }

        userRepository.deleteById(userId);
    }

    private boolean checkUser(User userForCheck){
        return userForCheck.getId() == null || userForCheck.getUsername() == null ||
            userForCheck.getEmail() == null
            || userForCheck.getSlackId() == null || userForCheck.getNotificationType() == null;
    }


}
