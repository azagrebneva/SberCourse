package orm.dao;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Map;

public interface RecipeDao {

    void addRecipe(String recipeName, Map<String, Integer> ingredients);

    void deleteRecipe(String recipeName);

    void showRecipes(String recipeName);
}
