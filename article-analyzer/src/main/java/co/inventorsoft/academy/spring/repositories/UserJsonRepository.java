package co.inventorsoft.academy.spring.repositories;

import co.inventorsoft.academy.spring.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Data access layer for User entity to get them from json file using Gson library.
 */
@Repository
public class UserJsonRepository {
    @Value("${USERS_FILE}")
    String usersFile;
    private final Gson gson;

    @Autowired
    public UserJsonRepository(Gson gson) {
        this.gson = gson;
    }

    public Set<User> getAllUsers() {
        try (Reader reader = new InputStreamReader(
            Objects.requireNonNull(getClass().getResourceAsStream(this.usersFile)))) {
            Type userListType = new TypeToken<Set<User>>(){}.getType();
            return gson.fromJson(reader, userListType);
        } catch (IOException e) {
            System.out.println("Something went wrong\n" + e.getMessage());
        }
        return new HashSet<>();
    }
    public User getUserById(Long userId){
        for(User user : getAllUsers()){
            if(user.getId().equals(userId)){
                return user;
            }
        }
        throw new IllegalArgumentException("User not found");
    }

}
