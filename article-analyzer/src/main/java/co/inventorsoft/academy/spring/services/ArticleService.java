package co.inventorsoft.academy.spring.services;

import co.inventorsoft.academy.spring.models.Article;
import co.inventorsoft.academy.spring.repositories.ArticleJsonRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Article service to operate article repository and make further actions in case of necessity.
 */
@Service
public class ArticleService {
    private final ArticleJsonRepository articleJsonRepository;

    @Autowired
    public ArticleService(ArticleJsonRepository articleJsonRepository) {
        this.articleJsonRepository = articleJsonRepository;
    }

    public List<Article> getAllArticles(){
        return articleJsonRepository.getArticles();
    }
}
