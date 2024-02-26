package project.Challenge_6BinarFood.model.merchant;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import project.Challenge_6BinarFood.model.product.Product;
import project.Challenge_6BinarFood.model.user.User;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "merchant")
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
//    @Id
//    @GeneratedValue(generator = "uuid-hibernate-generator")
//    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @Size(max = 50)
    @Column(name = "merchant_name")
    private String merchantName;
    @Size(max = 50)
    @Column(name = "merchant_location")
    private String merchantLocation;
    private boolean open;

    @OneToMany(mappedBy = "merchant")
    private List<Product> product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}