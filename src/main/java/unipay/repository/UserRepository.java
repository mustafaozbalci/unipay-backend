package unipay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unipay.entity.User;

import java.util.Optional;

/**
 * Repository interface for User entities.
 * Extends JpaRepository to provide CRUD operations and custom lookups by username or email.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by their unique username.
     *
     * @param username the username to search for
     * @return an Optional containing the User if found, or empty if not
     */
    Optional<User> findByUsername(String username);

    /**
     * Find a user by their unique email address.
     *
     * @param email the email to search for
     * @return an Optional containing the User if found, or empty if not
     */
    Optional<User> findByEmail(String email);

    /**
     * Check whether a user exists with the given email.
     *
     * @param email the email to check
     * @return true if a user with that email exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Check whether a user exists with the given username.
     *
     * @param username the username to check
     * @return true if a user with that username exists, false otherwise
     */
    boolean existsByUsername(String username);
}
