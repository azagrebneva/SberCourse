package orm.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import orm.model.Product;
import orm.model.Recipe;
import orm.repository.RecipeRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RecipeDaoImpl implements RecipeDao {

    private final EntityManager entityManager;

    private final TransactionTemplate transactionTemplate;

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeDaoImpl(EntityManager entityManager, TransactionTemplate transactionTemplate, RecipeRepository recipeRepository) {
        this.entityManager = entityManager;
        this.transactionTemplate = transactionTemplate;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void addRecipe(String recipeName, Map<String, Integer> ingredients) {

        Recipe recipe = new Recipe(recipeName);
        System.out.println(ingredients);

        Map<String, Product> productMap = new HashMap<>();

        // получить все продукты из базы
        List<Product> existedProducts = getAllProductsByNames(ingredients.keySet());
        existedProducts.forEach(p -> productMap.put(p.getName(), p));
        System.out.println("existedProducts in base" + existedProducts);

        // выбрать новые продукты
        Set<String> existedProductsNames = existedProducts.stream().map(Product::getName).collect(Collectors.toSet());
        Set<String> createdProductNames = ingredients.keySet().stream()
                .filter(p -> !existedProductsNames.contains(p))
                .collect(Collectors.toSet());

        // создать новые продукты
        List<Product> createdProducts = new ArrayList<>();
        for (String name: createdProductNames) {
            Product product = new Product(name);
            createdProducts.add(product);
            productMap.put(name, product);
        }

        doInTransaction(em -> {
            em.persist(recipe);
            createdProducts.forEach(em::persist);

            for (Map.Entry<String, Integer> ingredient: ingredients.entrySet()){
                String productName = ingredient.getKey();
                Product product = productMap.get(productName);
                Integer amount = ingredient.getValue();
                recipe.addRecipeComponent(product, amount);
            }

            em.flush();
        });
    }

    @Override
    public void deleteRecipe(String recipeName) {
        doInTransaction(em -> {
            List<Recipe> recipes = recipeRepository.findByName(recipeName);
            for (Recipe recipe : recipes) {
                em.remove(recipe);
            }
            em.flush();
        });
    }

    @Override
    public void showRecipes(String recipeName) {
        List<Recipe> recipes = doResultInTransaction(em -> {
            return recipeRepository.findByNameLike(recipeName + "%");
        });
        recipes.forEach(r -> System.out.println(r.getTextRecipe()));
    }

    @Transactional(readOnly = true)
    protected List<Product> getAllProductsByNames(Set<String> ingredients) {
        String productNames = "\'" + String.join("', '", ingredients) + "\'";
        Query query = entityManager.createQuery(
                "select p " +
                        "from Product p "+
                        "where p.name in ("+productNames+ ")"
        );
        return query.getResultList();
    }

    private void doInTransaction(Consumer<EntityManager> consumer) {
        transactionTemplate.executeWithoutResult(transactionStatus -> consumer.accept(entityManager));
    }

    private <T> T doResultInTransaction(Function<EntityManager, T> function) {
        return transactionTemplate.execute(status -> function.apply(entityManager));
    }
}
