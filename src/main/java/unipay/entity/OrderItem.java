package unipay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a single item within an order.
 * Maps to the "order_items" table in the database.
 */
@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    /**
     * Primary key of the order item (auto-generated).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Reference to the parent Order.
     */
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /**
     * Name of the product ordered.
     */
    @Column(nullable = false)
    private String itemName;

    /**
     * Quantity of this product in the order.
     */
    @Column(nullable = false)
    private Integer quantity;

    /**
     * Price per unit of the product.
     */
    @Column(nullable = false)
    private Double price;
}
