package co.inventorsoft.academy.spring.repositories;

import co.inventorsoft.academy.spring.models.Article;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;


/**
 * Data access layer for Article entity to get them from json file using Gson library.
 */
@Repository
public class ArticleJsonRepository {
    @Value("${ARTICLES_FILE}")
    String articlesFile;
    private final Gson gson;

    @Autowired
    public ArticleJsonRepository(Gson gson) {
        this.gson = gson;
    }

    public List<Article> getArticles(){
        try (Reader reader = new InputStreamReader(
            Objects.requireNonNull(getClass().getResourceAsStream(this.articlesFile)))) {
            Type userListType = new TypeToken<List<Article>>(){}.getType();
            return gson.fromJson(reader, userListType);
        } catch (IOException e) {
            System.out.println("Something went wrong\n" + e.getMessage());
        }
        return new ArrayList<>();
    }

}
