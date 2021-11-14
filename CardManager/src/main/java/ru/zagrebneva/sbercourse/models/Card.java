package ru.zagrebneva.sbercourse.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;
import ru.zagrebneva.sbercourse.models.validators.CardNumberConstraint;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
@Table(name = "card")
public class Card implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "number")
    @CardNumberConstraint
    @NotEmpty(message = "Number should not be empty.")
    private String number;

    @Column(name = "cvv")
    @NotEmpty(message = "Cvv code should not be empty.")
    @Pattern(regexp = "[0-9]+", message = "Cvv consists of numbers.")
    @Size(min = 3, max = 3, message = "Cvv code consists of 3 symbols.")
    private String cvv;

    @Column(name = "date_of_issue")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfIssue;

    @Column(name = "card_expiry_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date cardExpiryDate;

    @Column(name = "pincode_hash")
    private String pincodeHash;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CardStatus status;

    @ManyToOne
    @JoinColumn(name = "cusomer_id",
            foreignKey = @ForeignKey(name = "customer_id_fk")
    )
    private Customer customer;

    public Card(Customer customer) {
        this.customer = customer;
        this.status = CardStatus.OPENED;
    }

    @Override
    public String toString() {
        return  number + ", valid thru " + toCardDateFormat(cardExpiryDate) +
                ((customer == null) ? "": ", card holder ") + customer;
    }

    public String toCardDateFormat(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/yyyy");
        return formatter.format(date);
    }
}
