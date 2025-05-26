package unipay.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unipay.dto.*;
import unipay.entity.User;
import unipay.exception.BalanceNotEnoughException;
import unipay.exception.EmailAlreadyExistsException;
import unipay.exception.UserNotFoundException;
import unipay.exception.UsernameAlreadyExistsException;
import unipay.mapper.UserMapper;
import unipay.repository.UserRepository;

/**
 * Service for user registration, authentication, profile retrieval,
 * and balance management.
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    /**
     * Constructs the UserService with required dependencies.
     *
     * @param userRepository  repository for User entities
     * @param passwordEncoder encoder for user passwords
     * @param userMapper      mapper to convert User entities to DTOs
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    /**
     * Registers a new user after validating that the username and email
     * are not already in use. Encodes the password and saves the entity.
     *
     * @param req registration details from client
     * @return the saved user as a UserResponse DTO
     * @throws UsernameAlreadyExistsException if the username is taken
     * @throws EmailAlreadyExistsException    if the email is taken
     */
    @Transactional
    public UserResponse registerUser(UserRegisterRequest req) {
        logger.info("Registering user '{}'", req.getUsername());
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setEmail(req.getEmail());
        user.setStudentNumber(req.getStudentNumber());
        if (req.getPlate() != null) {
            user.setPlate(req.getPlate().trim().toUpperCase());
        }
        User saved = userRepository.save(user);
        logger.info("User '{}' registered", saved.getUsername());
        return userMapper.toUserResponse(saved);
    }

    /**
     * Authenticates a user by email and password.
     *
     * @param req login credentials from client
     * @return authenticated user as a UserResponse DTO
     * @throws UserNotFoundException if no user exists with the given email
     * @throws RuntimeException      if the password does not match
     */
    @Transactional(readOnly = true)
    public UserResponse loginUser(UserLoginRequest req) {
        logger.info("Authenticating '{}'", req.getEmail());
        User user = userRepository.findByEmail(req.getEmail()).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            logger.warn("Authentication failed for '{}'", req.getEmail());
            throw new RuntimeException("Invalid credentials");
        }
        logger.info("Authentication successful for '{}'", req.getEmail());
        return userMapper.toUserResponse(user);
    }

    /**
     * Retrieves a User entity by username.
     *
     * @param username the username to look up
     * @return the matching User entity
     * @throws UserNotFoundException if no user exists with that username
     */
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        logger.debug("Fetching user by username '{}'", username);
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found: " + username));
    }

    /**
     * Retrieves a User entity by ID.
     *
     * @param id the user ID to look up
     * @return the matching User entity
     * @throws UserNotFoundException if no user exists with that ID
     */
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        logger.debug("Fetching user by id {}", id);
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found: " + id));
    }

    /**
     * Retrieves public details for the given username.
     *
     * @param username the username of the user
     * @return the user details as a UserResponse DTO
     * @throws UserNotFoundException if no user exists with that username
     */
    @Transactional(readOnly = true)
    public UserResponse getUserDetails(String username) {
        logger.info("Retrieving details for '{}'", username);
        return userMapper.toUserResponse(getUserByUsername(username));
    }

    /**
     * Updates the authenticated user's password.
     *
     * @param username the username of the account to update
     * @param req      containing the new password
     * @return updated user details as a UserResponse DTO
     * @throws UserNotFoundException if no user exists with that username
     */
    @Transactional
    public UserResponse updateUserPassword(String username, UserPasswordUpdateRequest req) {
        logger.info("Updating password for '{}'", username);
        User user = getUserByUsername(username);
        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        User updated = userRepository.save(user);
        logger.info("Password updated for '{}'", username);
        return userMapper.toUserResponse(updated);
    }

    /**
     * Adds the specified amount to the user's balance.
     *
     * @param username the username of the account
     * @param req      containing the amount to add
     * @return updated user details as a UserResponse DTO
     * @throws UserNotFoundException if no user exists with that username
     */
    @Transactional
    public UserResponse addBalance(String username, UserAddBalanceRequest req) {
        logger.info("Adding {} to '{}'", req.getAmountToAdd(), username);
        User user = getUserByUsername(username);
        double newBal = user.getBalance() + req.getAmountToAdd();
        user.setBalance(newBal);
        User updated = userRepository.save(user);
        logger.info("New balance for '{}': {}", username, updated.getBalance());
        return userMapper.toUserResponse(updated);
    }

    /**
     * Increments a user's balance by the given amount without returning a DTO.
     *
     * @param username the username of the account
     * @param amount   the amount to add (may be negative)
     * @throws UserNotFoundException if no user exists with that username
     */
    @Transactional
    public void updateUserBalance(String username, Double amount) {
        logger.debug("Updating balance by {} for '{}'", amount, username);
        User user = getUserByUsername(username);
        double newBal = user.getBalance() + amount;
        user.setBalance(newBal);
        userRepository.save(user);
        logger.debug("Balance updated for '{}': {}", username, newBal);
    }

    /**
     * Checks if a user has sufficient balance and deducts the specified amount.
     *
     * @param userId the ID of the user
     * @param amount the amount to deduct
     * @throws UserNotFoundException     if no user exists with that ID
     * @throws BalanceNotEnoughException if the user's balance is insufficient
     */
    @Transactional
    public void checkAndDecreaseBalance(Long userId, double amount) {
        logger.info("Checking balance for userId={} amount={}", userId, amount);
        User user = getUserById(userId);
        if (user.getBalance() < amount) {
            logger.warn("Insufficient balance for userId={}", userId);
            throw new BalanceNotEnoughException("Insufficient balance");
        }
        user.setBalance(user.getBalance() - amount);
        userRepository.save(user);
        logger.info("Balance decreased for userId={}. New balance={}", userId, user.getBalance());
    }

    /**
     * Refunds the specified amount to the user's balance.
     *
     * @param userId the ID of the user
     * @param amount the amount to refund
     * @throws UserNotFoundException if no user exists with that ID
     */
    @Transactional
    public void refundBalance(Long userId, double amount) {
        logger.info("Refunding {} to userId={}", amount, userId);
        User user = getUserById(userId);
        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);
        logger.info("Refund complete for userId={}. New balance={}", userId, user.getBalance());
    }

    /**
     * Updates the authenticated user's vehicle plate number.
     *
     * @param username the username of the account
     * @param req      containing the new plate
     * @return updated user details as a UserResponse DTO
     * @throws UserNotFoundException if no user exists with that username
     */
    @Transactional
    public UserResponse updateUserPlate(String username, UserPlateUpdateRequest req) {
        logger.info("Updating plate for '{}': {}", username, req.getPlate());
        User user = getUserByUsername(username);
        String cleaned = req.getPlate().trim().toUpperCase();
        user.setPlate(cleaned);
        User updated = userRepository.save(user);
        logger.info("Plate updated for '{}': {}", username, updated.getPlate());
        return userMapper.toUserResponse(updated);
    }
}
