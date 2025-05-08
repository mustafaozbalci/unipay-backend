// src/main/java/unipay/controller/ParkingController.java
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

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    private final ParkingService parkingService;
    private final UserService userService;
    private final ParkingMapper parkingMapper;

    public ParkingController(ParkingService parkingService, UserService userService, ParkingMapper parkingMapper) {
        this.parkingService = parkingService;
        this.userService = userService;
        this.parkingMapper = parkingMapper;
    }

    @PostMapping("/enter")
    public ParkingSessionDto enter(@AuthenticationPrincipal UserDetails auth, @RequestParam("parkingAreaId") Long parkingAreaId) {

        if (auth == null) {
            throw new SecurityException("Otoparkı kullanmak için giriş yapmalısınız");
        }
        Long userId = userService.getUserByUsername(auth.getUsername()).getId();
        var session = parkingService.enterVehicle(parkingAreaId, userId, null);
        return parkingMapper.toDto(session);
    }

    @PostMapping("/exit")
    public ParkingSessionDto exit(@RequestParam("sessionId") Long sessionId) {
        var session = parkingService.exitVehicle(sessionId);
        return parkingMapper.toDto(session);
    }

    @GetMapping("/history")
    public List<ParkingSessionDto> history(@AuthenticationPrincipal UserDetails auth) {
        if (auth == null) {
            throw new SecurityException("Önce giriş yapmalısınız");
        }
        Long userId = userService.getUserByUsername(auth.getUsername()).getId();
        return parkingService.getUserHistory(userId).stream().map(parkingMapper::toDto).collect(Collectors.toList());
    }

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
     * Verilen sessionId için, oturum devam ediyorsa
     * şu ana kadar biriken ücreti anlık döner.
     */
    @GetMapping("/current-fee")
    public BigDecimal currentFee(@RequestParam("sessionId") Long sessionId) {
        return parkingService.getCurrentFee(sessionId);
    }
}
