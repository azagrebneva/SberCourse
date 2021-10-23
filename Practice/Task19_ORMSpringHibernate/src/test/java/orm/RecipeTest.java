package orm;

import org.h2.tools.Server;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import orm.dao.RecipeDao;

import java.sql.SQLException;
import java.util.*;

@SpringBootTest
@EntityScan("orm")
public class RecipeTest {

    @Autowired
    private RecipeDao recipeDao;

    @BeforeAll
    public static void startServer() throws SQLException {
        Server.createTcpServer().start();
    }

    @Test
    public void testDaoRecipes() {
        Map<String, Integer> ingredients = new LinkedHashMap<>(){{
            put("tomato", 300);
            put("cucumber", 150);
            put("dill", 50);
            put("salt", 5);
        }};
        recipeDao.addRecipe("summer salad 1", ingredients);
        ingredients = new LinkedHashMap<>(){{
            put("cabbage", 300);
            put("cucumber", 150);
            put("salt", 5);
        }};
        recipeDao.addRecipe("summer salad 2", ingredients);

        recipeDao.showRecipes("summer");

        recipeDao.deleteRecipe("summer salad 1");
        System.out.println("\n----------------------" +
                "\n After removal");
        recipeDao.showRecipes("summer");
    }

}
