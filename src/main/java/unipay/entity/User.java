package unipay.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

/**
 * Entity representing a system user.
 * Maps to the "users" database table.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    /**
     * Primary key of the user (auto-generated).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique username for login (natural identifier).
     */
    @NaturalId
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Encrypted password for authentication.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Unique email address of the user.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Student number associated with the user.
     */
    @Column(name = "student_number", unique = true)
    private String studentNumber;

    /**
     * Current prepaid balance of the user's account.
     */
    @Column(nullable = false)
    private Double balance = 0.0;

    /**
     * Registered vehicle plate number for parking access (optional).
     */
    @Column(nullable = true)
    private String plate;
}
