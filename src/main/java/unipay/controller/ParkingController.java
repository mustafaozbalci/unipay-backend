package unipay.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import unipay.dto.ParkingSessionDto;
import unipay.mapper.ParkingMapper;
import unipay.service.ParkingService;
import unipay.service.UserService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing parking operations.
 * Provides endpoints to:
 * - Enter a parking area (create a new session)
 * - Exit a parking session (complete the session)
 * - Retrieve the authenticated user's parking history
 * - Retrieve all sessions (ADMIN only)
 * - Get the current accumulated fee for an ongoing session
 */
@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    private final ParkingService parkingService;
    private final UserService userService;
    private final ParkingMapper parkingMapper;

    /**
     * Constructs a new ParkingController with required services and mapper.
     *
     * @param parkingService service handling parking logic
     * @param userService    service for retrieving user details
     * @param parkingMapper  mapper to convert entities to DTOs
     */
    public ParkingController(ParkingService parkingService, UserService userService, ParkingMapper parkingMapper) {
        this.parkingService = parkingService;
        this.userService = userService;
        this.parkingMapper = parkingMapper;
    }

    /**
     * Registers vehicle entry into the specified parking area.
     * Requires the user to be authenticated.
     *
     * @param auth          injected authentication principal
     * @param parkingAreaId the ID of the parking area to enter
     * @return a DTO representing the new parking session
     * @throws SecurityException if the user is not authenticated
     */
    @PostMapping("/enter")
    public ParkingSessionDto enter(@AuthenticationPrincipal UserDetails auth, @RequestParam("parkingAreaId") Long parkingAreaId) {
        if (auth == null) {
            throw new SecurityException("Otoparkı kullanmak için giriş yapmalısınız");
        }
        Long userId = userService.getUserByUsername(auth.getUsername()).getId();
        var session = parkingService.enterVehicle(parkingAreaId, userId, null);
        return parkingMapper.toDto(session);
    }

    /**
     * Registers vehicle exit for a given parking session.
     *
     * @param sessionId the ID of the parking session to complete
     * @return a DTO representing the completed parking session
     */
    @PostMapping("/exit")
    public ParkingSessionDto exit(@RequestParam("sessionId") Long sessionId) {
        var session = parkingService.exitVehicle(sessionId);
        return parkingMapper.toDto(session);
    }

    /**
     * Retrieves the parking session history for the authenticated user.
     * Requires the user to be authenticated.
     *
     * @param auth injected authentication principal
     * @return a list of DTOs for the user's past parking sessions
     * @throws SecurityException if the user is not authenticated
     */
    @GetMapping("/history")
    public List<ParkingSessionDto> history(@AuthenticationPrincipal UserDetails auth) {
        if (auth == null) {
            throw new SecurityException("Önce giriş yapmalısınız");
        }
        Long userId = userService.getUserByUsername(auth.getUsername()).getId();
        return parkingService.getUserHistory(userId).stream().map(parkingMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Retrieves all parking session history for ADMIN users.
     * Access restricted to users with ADMIN role.
     *
     * @param auth injected authentication principal
     * @return a list of DTOs for all sessions
     * @throws SecurityException if the user is not authenticated
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/history")
    public List<ParkingSessionDto> adminHistory(@AuthenticationPrincipal UserDetails auth) {
        if (auth == null) {
            throw new SecurityException("Önce giriş yapmalısınız");
        }
        Long adminId = userService.getUserByUsername(auth.getUsername()).getId();
        return parkingService.getAllSessionsForAdmin(adminId).stream().map(parkingMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Returns the current accumulated fee for an ongoing parking session.
     *
     * @param sessionId the ID of the active parking session
     * @return the up-to-date fee as BigDecimal
     */
    @GetMapping("/current-fee")
    public BigDecimal currentFee(@RequestParam("sessionId") Long sessionId) {
        return parkingService.getCurrentFee(sessionId);
    }
}
