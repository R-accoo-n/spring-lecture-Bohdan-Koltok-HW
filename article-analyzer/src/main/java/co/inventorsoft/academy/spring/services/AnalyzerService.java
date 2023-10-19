package co.inventorsoft.academy.spring.services;

import co.inventorsoft.academy.spring.models.NotificationType;
import co.inventorsoft.academy.spring.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Analyzer service who operates notifications, articles, users and categories services.
 */
@Service
public class AnalyzerService {
    private final ArticleService articleService;
    private final CategoriesService categoriesService;
    private final NotificationSlackService notificationSlackService;
    private final NotificationEmailService notificationEmailService;
    private final UserService userService;

    @Autowired
    public AnalyzerService(ArticleService articleService, CategoriesService categoriesService,
                           NotificationSlackService notificationSlackService,
                           NotificationEmailService notificationEmailService,
                           UserService userService) {
        this.articleService = articleService;
        this.categoriesService = categoriesService;
        this.notificationSlackService = notificationSlackService;
        this.notificationEmailService = notificationEmailService;
        this.userService = userService;
    }

    public void analyze(){
        this.categoriesService.processCategories(this.articleService.getAllArticles());

        for (User user : this.userService.getAllUsersFromJson()){
            if (user.getNotificationType().equals(NotificationType.EMAIL)) {
                this.notificationEmailService.notifyUser(user.getId(),
                    "Your articles has been proceeded email");
            } else {
                this.notificationSlackService.notifyUser(user.getId(),
                    "Your articles has been proceeded slack");
            }

        }
    }
}
