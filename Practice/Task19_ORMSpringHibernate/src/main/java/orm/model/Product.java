package orm.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.AccessType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


//@NamedQueries({
//})

//Phone phone = entityManager
//        .createNamedQuery( "get_phone_by_number", Phone.class )
//        .setParameter( "number", "123-456-7890" )
//        .getSingleResult();
@Entity
@AccessType(AccessType.Type.FIELD)
@NoArgsConstructor
@Data
@ToString
@NamedQuery(
    name = "get_products_by_names",
    query = "select p " +
        "from Product p " +
        //    "where p.name in ('tomato', 'salt')"
        "where p.name in (:names)"
)
public class Product implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;
    @Basic
    private String name;

    //@OneToMany(
    //        mappedBy = "product",
    //        cascade = CascadeType.ALL,
     //       orphanRemoval = true//,
            //fetch = FetchType.EAGER
    //)
    //@Fetch(FetchMode.JOIN)
    //private List<RecipeComponent> recipes = new ArrayList<>();

    public Product(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id) && name.equals(product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
