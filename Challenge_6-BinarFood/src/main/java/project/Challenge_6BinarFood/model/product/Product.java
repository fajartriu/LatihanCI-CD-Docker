package project.Challenge_6BinarFood.model.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import project.Challenge_6BinarFood.model.merchant.Merchant;
import project.Challenge_6BinarFood.model.order.OrderDetail;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Size(max = 50)
    @Column(name = "product_name")
    private String productName;
    private Double price;
    private boolean deleteProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", referencedColumnName = "id")
    private Merchant merchant;

    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetail;
}
