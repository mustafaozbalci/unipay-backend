// src/main/java/unipay/service/UserService.java
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

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

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

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        logger.debug("Fetching user by username '{}'", username);
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found: " + username));
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        logger.debug("Fetching user by id {}", id);
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found: " + id));
    }

    @Transactional(readOnly = true)
    public UserResponse getUserDetails(String username) {
        logger.info("Retrieving details for '{}'", username);
        return userMapper.toUserResponse(getUserByUsername(username));
    }

    @Transactional
    public UserResponse updateUserPassword(String username, UserPasswordUpdateRequest req) {
        logger.info("Updating password for '{}'", username);
        User user = getUserByUsername(username);
        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        User updated = userRepository.save(user);
        logger.info("Password updated for '{}'", username);
        return userMapper.toUserResponse(updated);
    }

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

    @Transactional
    public void updateUserBalance(String username, Double amount) {
        logger.debug("Updating balance by {} for '{}'", amount, username);
        User user = getUserByUsername(username);
        double newBal = user.getBalance() + amount;
        user.setBalance(newBal);
        userRepository.save(user);
        logger.debug("Balance updated for '{}': {}", username, newBal);
    }

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

    @Transactional
    public void refundBalance(Long userId, double amount) {
        logger.info("Refunding {} to userId={}", amount, userId);
        User user = getUserById(userId);
        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);
        logger.info("Refund complete for userId={}. New balance={}", userId, user.getBalance());
    }

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
