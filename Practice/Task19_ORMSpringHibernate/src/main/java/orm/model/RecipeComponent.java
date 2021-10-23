package orm.model;

import lombok.*;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
public class RecipeComponent implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id",
            foreignKey = @ForeignKey(name = "PRODUCT_ID_FK")
    )
    private Product product;

    @Basic
    private Integer amount;

    public RecipeComponent(Recipe recipe, Product product) {
        this.recipe = recipe;
        this.product = product;
    }

    public RecipeComponent(Recipe recipe, Product product, Integer amount) {
        this.recipe = recipe;
        this.product = product;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeComponent component = (RecipeComponent) o;
        return recipe.equals(component.recipe) &&
                product.equals(component.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipe, product);
    }

    @Override
    public String toString() {
        return "RecipeComponent{" +
                "recipe=" + (recipe != null ? recipe.getId(): null) +
                ", product=" + (product != null ? product.getName(): null) +
                ", amount=" + amount +
                '}';
    }

    //    @ManyToOne
//    @JoinColumnsOrFormulas({
//            @JoinColumnOrFormula(formula = @JoinFormula(
//                    value = "(select p.id from product p where p.uuid = uuid)", referencedColumnName = "id")),
//            @JoinColumnOrFormula(column = @JoinColumn("uuidOfA", referencedColumnName = "uuid"))
//    })
//    private Product product;


//    @Override
//    public String toString() {
//        return "Component{" +
//                "id=" + id +
//                ", amount=" + amount +
//                ", recipe=" + (recipe != null ? recipe.getId() + ", " +
//                        recipe.getName(): null) +
//                '}';
//    }
}
