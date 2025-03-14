package unipay.dto;

import lombok.Data;

@Data
public class PaymentCardDTO {
    private String cardHolderName;
    private String cardNumber;
    private String expireMonth;
    private String expireYear;
    private String cvc;
    private int registerCard;
}
