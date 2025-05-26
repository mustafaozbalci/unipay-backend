package unipay.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO for deposit requests through the payment gateway.
 * Contains information about the transaction amount and card details.
 */
@Data
public class PaymentDepositRequestDTO {

    /**
     * The amount to be charged before fees.
     */
    private BigDecimal price;

    /**
     * The final amount to be paid after any discounts or fees.
     */
    private BigDecimal paidPrice;

    /**
     * Card details used to process the payment.
     */
    private PaymentCardDTO paymentCard;
}
