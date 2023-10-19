package co.inventorsoft.academy.spring.services;

import co.inventorsoft.academy.spring.models.Article;
import co.inventorsoft.academy.spring.repositories.CategoryJsonRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * Categories service to process articles into categories and operate category repository.
 */
@Service
public class CategoriesService {
    @Value("${excluded.words}")
    private String stopWords;

    private final CategoryJsonRepository categoryJsonRepository;

    @Autowired
    public CategoriesService(CategoryJsonRepository categoryJsonRepository) {
        this.categoryJsonRepository = categoryJsonRepository;
    }

    private Set<String> processArticlesIntoCategories(List<Article> articles){
        List<String> processedStopWords = Arrays.asList(stopWords.split(", "));
        Set<String> categories = new HashSet<>();

        for (Article article : articles) {
            Map<String, Integer> articleFrequency = new HashMap<>();
            String[] words = article.getContent().toLowerCase().split("[\\W\\s]+");

            for (String word : words) {
                if (!processedStopWords.contains(word)) {
                    articleFrequency.put(word, articleFrequency.getOrDefault(word, 0) + 1);
                }
            }

            if (!articleFrequency.isEmpty()) {
                int maxCount = Collections.max(articleFrequency.values());
                Set<String> maxWords = new HashSet<>();

                for(Map.Entry<String, Integer> entry : articleFrequency.entrySet()){
                    if(entry.getValue().equals(maxCount)){
                        maxWords.add(entry.getKey());
                    }
                }

                categories.addAll(maxWords);
            } else {
                System.out.println("" + article.getName() + " is empty, select another article");
            }
        }
        return categories;

    }

    private void saveCategories(Set<String> categories){
        System.out.println(categories.toString());
        this.categoryJsonRepository.saveCategories(categories);
    }

    public void processCategories(List<Article> articles){
        Set<String> categories = processArticlesIntoCategories(articles);
        saveCategories(categories);
    }


}
