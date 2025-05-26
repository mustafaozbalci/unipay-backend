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

/**
 * Controller for handling deposit operations via Iyzico.
 * Offers an endpoint to add funds to the authenticated user's balance.
 */
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final IyzicoPaymentService paymentService;
    private final UserService userService;

    /**
     * Initialize with payment and user services.
     *
     * @param paymentService service integrating with Iyzico APIs
     * @param userService    service for user data and balance updates
     */
    public PaymentController(IyzicoPaymentService paymentService, UserService userService) {
        this.paymentService = paymentService;
        this.userService = userService;
    }

    /**
     * Process a deposit request for the authenticated user.
     * Calls Iyzico to perform the payment, then updates the user’s balance on success.
     *
     * @param auth the authenticated user principal
     * @param dto  deposit details including amount and payment info
     * @return 200 OK with the Iyzico Payment object if status is SUCCESS;
     * 400 Bad Request with error message otherwise
     * @throws Exception if an error occurs during payment processing
     */
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
