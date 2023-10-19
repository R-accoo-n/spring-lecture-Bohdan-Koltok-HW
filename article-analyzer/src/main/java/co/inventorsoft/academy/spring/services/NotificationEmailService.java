package co.inventorsoft.academy.spring.services;

import co.inventorsoft.academy.spring.models.NotificationType;
import co.inventorsoft.academy.spring.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class NotificationEmailService implements NotificationService {
    private final UserService userService;
    @Autowired
    public NotificationEmailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void notifyUser(Long userId, String notification){
        User currentUser = this.userService.getUserByIdFromJson(userId);

        if(currentUser.getNotificationType().equals(NotificationType.EMAIL)){
            System.out.println("Dear " + currentUser.getUsername() + ", you have new notification:\n" + notification);
        }

    }

}
