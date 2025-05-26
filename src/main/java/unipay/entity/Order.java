package unipay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing a customer's order.
 * Maps to the "orders" table in the database.
 */
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    /**
     * Primary key of the order (auto-generated).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User who placed this order.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Restaurant where the order was placed.
     */
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    /**
     * Items included in this order.
     * Cascade all operations and remove orphaned items.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    /**
     * Timestamp when the order was created.
     */
    @Column(nullable = false)
    private LocalDateTime orderTime;

    /**
     * Current status of the order (e.g., NEW, PREPARING, COMPLETED).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    /**
     * Total amount charged for the order.
     */
    @Column(nullable = false)
    private Double totalAmount;

    /**
     * Estimated preparation time in minutes (optional).
     */
    private Integer estimatedPreparationTime;
}
