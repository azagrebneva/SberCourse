package orm.repository;

import org.springframework.data.repository.CrudRepository;
import orm.model.Recipe;

import java.util.List;

public interface RecipeRepository  extends CrudRepository<Recipe, Long> {

    List<Recipe> findByNameLike(String name);

    List<Recipe> findByName(String name);
}
