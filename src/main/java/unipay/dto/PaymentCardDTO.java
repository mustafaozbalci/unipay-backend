package unipay.dto;

import lombok.Data;

/**
 * DTO for capturing payment card information.
 * Used when processing or storing card details for transactions.
 */
@Data
public class PaymentCardDTO {

    /**
     * Name of the cardholder as printed on the card.
     */
    private String cardHolderName;

    /**
     * Primary account number of the card (no spaces).
     */
    private String cardNumber;

    /**
     * Two-digit expiration month (e.g., "07").
     */
    private String expireMonth;

    /**
     * Four-digit expiration year (e.g., "2027").
     */
    private String expireYear;

    /**
     * Card verification code (CVC/CVV) security value.
     */
    private String cvc;

    /**
     * Flag indicating whether to save this card for future use (0 = no, 1 = yes).
     */
    private int registerCard;
}
