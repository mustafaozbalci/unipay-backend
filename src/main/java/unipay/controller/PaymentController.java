package unipay.controller;

import unipay.dto.PaymentDepositRequestDTO;
import unipay.service.IyzicoPaymentService;
import unipay.service.UserService;
import com.iyzipay.model.Payment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final IyzicoPaymentService paymentService;
    private final UserService userService;

    public PaymentController(IyzicoPaymentService paymentService, UserService userService) {
        this.paymentService = paymentService;
        this.userService = userService;
    }
    //BAKİYE EKLEME
    @PostMapping("/deposit")
    public ResponseEntity<Object> depositMoney(@RequestHeader("username") String username, @RequestBody PaymentDepositRequestDTO dto) {
        try {
            Payment payment = paymentService.depositMoney(dto);
            if ("SUCCESS".equalsIgnoreCase(payment.getStatus())) {
                userService.updateUserBalance(username, dto.getPrice().doubleValue());
                return ResponseEntity.ok(payment);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(payment.getErrorMessage());
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment creation failed: " + e.getMessage());
        }
    }

}
