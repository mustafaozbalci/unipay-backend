package unipay.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentDepositRequestDTO {
    private BigDecimal price;
    private BigDecimal paidPrice;
    private PaymentCardDTO paymentCard;
}
