// src/main/java/unipay/controller/PaymentController.java
package unipay.controller;

import com.iyzipay.model.Payment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unipay.dto.PaymentDepositRequestDTO;
import unipay.service.IyzicoPaymentService;
import unipay.service.UserService;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final IyzicoPaymentService paymentService;
    private final UserService userService;

    public PaymentController(IyzicoPaymentService paymentService, UserService userService) {
        this.paymentService = paymentService;
        this.userService = userService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> depositMoney(@AuthenticationPrincipal UserDetails auth, @RequestBody PaymentDepositRequestDTO dto) throws Exception {

        Payment payment = paymentService.depositMoney(dto);
        if ("SUCCESS".equalsIgnoreCase(payment.getStatus())) {
            String username = auth.getUsername();
            userService.updateUserBalance(username, dto.getPrice().doubleValue());
            return ResponseEntity.ok(payment);
        }
        return ResponseEntity.badRequest().body(payment.getErrorMessage());
    }
}
