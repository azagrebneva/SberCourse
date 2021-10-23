package orm.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NaturalId;
import org.springframework.data.annotation.AccessType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@AccessType(AccessType.Type.FIELD)
@NoArgsConstructor
@Data
@ToString
public class Recipe implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    @NaturalId
    @Basic
    private String name;

    //    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(
            mappedBy = "recipe",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<RecipeComponent> components = new ArrayList<>();
    //private Set<RecipeComponent> components = new HashSet<>();

    public Recipe(String name) {
        this.name = name;
    }

    public void addRecipeComponent(Product product, int amount) {
        RecipeComponent component = new RecipeComponent(this, product, amount);
        components.add(component);
        //product.getRecipes().add(component);
    }

    public void removeRecipeComponent(Product product) {
        RecipeComponent component = new RecipeComponent(this, product);
        components.remove(component);
        //product.getRecipes().remove(component);
    }

    public String getTextRecipe(){
        String recipe = name + ":";
        for (RecipeComponent component: components) {
            recipe += "\n\t" + component.getProduct().getName() +
                    component.getProduct().getId() +
                    " - " + component.getAmount() + " gr";
        }
        return recipe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return name.equals(recipe.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
