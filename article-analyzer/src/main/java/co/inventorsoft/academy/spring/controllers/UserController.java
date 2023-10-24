package co.inventorsoft.academy.spring.controllers;

import co.inventorsoft.academy.spring.models.User;
import co.inventorsoft.academy.spring.security.JwtUser;
import co.inventorsoft.academy.spring.services.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable("userId") Long id) {
        return userService.getUserById(id);
    }

    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{userId}")
    public User updateUser(@PathVariable("userId") Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Long id) {
        userService.deleteUserById(id);
    }

    @GetMapping("/me")
    public User getMyInfo(Authentication authentication) {
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        return userService.getUserByEmail(jwtUser.getEmail());
    }

    @GetMapping("/admin/info/{userId}")
    public User getUserInfoById(@PathVariable("userId") Long id) {
        return userService.getUserById(id);
    }
}
