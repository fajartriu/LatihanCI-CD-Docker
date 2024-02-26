package project.Challenge_6BinarFood.model.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import project.Challenge_6BinarFood.model.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDate order_time;
    @Size(max = 100)
    private String destination_address;
    private boolean completed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetail;
}
