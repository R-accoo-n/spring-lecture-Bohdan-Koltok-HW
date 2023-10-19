package co.inventorsoft.academy.spring.repositories;

import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;


/**
 * Data access layer for Categories to save them into json file after articles been processed into categories.
 */
@Repository
public class CategoryJsonRepository {
    @Value("${CATEGORIES_FILE_}")
    private Path savePath;
    private final Gson gson;

    @Autowired
    public CategoryJsonRepository(Gson gson) {
        this.gson = gson;
    }

    public void saveCategories(Set<String> categories){
        try(Writer writer = new FileWriter(this.savePath.toFile())){

            gson.toJson(categories, writer);

        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}
