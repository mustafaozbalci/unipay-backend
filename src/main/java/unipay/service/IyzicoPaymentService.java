package unipay.service;

import com.iyzipay.Options;
import com.iyzipay.model.*;
import com.iyzipay.request.CreatePaymentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import unipay.dto.PaymentCardDTO;
import unipay.dto.PaymentDepositRequestDTO;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Service integrating with Iyzico to handle deposit payments.
 * Validates amounts, constructs the request, and logs results.
 */
@Service
public class IyzicoPaymentService {

    private static final Logger logger = LoggerFactory.getLogger(IyzicoPaymentService.class);

    /**
     * Iyzico API options bean (injected from config)
     */
    private final Options options;

    /**
     * Initialize the service with Iyzico options.
     *
     * @param options configured Iyzico API options
     */
    public IyzicoPaymentService(Options options) {
        this.options = options;
    }

    /**
     * Processes a deposit by calling Iyzico.
     * First validates prices, then maps to CreatePaymentRequest
     * and sends the request. Logs success or failure.
     *
     * @param dto deposit details including amount and card info
     * @return the Iyzico Payment response
     * @throws Exception on Iyzico API errors
     */
    public Payment depositMoney(PaymentDepositRequestDTO dto) throws Exception {
        logger.info("Starting deposit: {}", dto.getPrice());
        // Validate price and paidPrice match
        validatePrices(dto.getPrice(), dto.getPaidPrice());

        // Send payment request to Iyzico
        Payment payment = Payment.create(mapToCreatePaymentRequest(dto), options);
        if ("SUCCESS".equalsIgnoreCase(payment.getStatus())) {
            logger.info("Deposit successful: conversationId={}", payment.getConversationId());
        } else {
            logger.warn("Deposit failed: {}", payment.getErrorMessage());
        }
        return payment;
    }

    /**
     * Ensures price and paidPrice are non-null and equal.
     *
     * @param price     original requested amount
     * @param paidPrice final amount to be paid
     */
    private void validatePrices(BigDecimal price, BigDecimal paidPrice) {
        logger.info("validatePrices() - Start, price: {}, paidPrice: {}", price, paidPrice);

        if (price == null || paidPrice == null) {
            logger.error("price or paidPrice is null");
            throw new IllegalArgumentException("price and paidPrice are required.");
        }
        if (price.compareTo(paidPrice) != 0) {
            logger.error("Mismatch between price and paidPrice. price: {}, paidPrice: {}", price, paidPrice);
            throw new IllegalArgumentException("price must equal paidPrice.");
        }

        logger.info("validatePrices() - End, amounts are valid.");
    }

    /**
     * Converts a PaymentDepositRequestDTO into Iyzico's CreatePaymentRequest.
     * Sets price, buyer, card, addresses, and basket details.
     *
     * @param dto deposit details from client
     * @return a fully populated CreatePaymentRequest
     */
    private CreatePaymentRequest mapToCreatePaymentRequest(PaymentDepositRequestDTO dto) {
        logger.info("mapToCreatePaymentRequest() - Start, dto: {}", dto);

        CreatePaymentRequest request = new CreatePaymentRequest();

        // A) Amounts
        request.setPrice(dto.getPrice());
        request.setPaidPrice(dto.getPaidPrice());
        request.setInstallment(1);

        // B) Default parameters
        request.setBasketId("DEP-" + System.currentTimeMillis());
        request.setPaymentChannel("WEB");
        request.setPaymentGroup("PRODUCT");
        request.setLocale("tr");
        request.setConversationId("conv-" + System.currentTimeMillis());
        request.setCurrency("TRY");

        // C) Card details
        PaymentCardDTO cardDTO = dto.getPaymentCard();
        if (cardDTO == null) {
            logger.error("PaymentCard is null");
            throw new IllegalArgumentException("PaymentCard information is required.");
        }
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setCardHolderName(cardDTO.getCardHolderName());
        paymentCard.setCardNumber(cardDTO.getCardNumber());
        paymentCard.setExpireMonth(cardDTO.getExpireMonth());
        paymentCard.setExpireYear(cardDTO.getExpireYear());
        paymentCard.setCvc(cardDTO.getCvc());
        paymentCard.setRegisterCard(cardDTO.getRegisterCard());
        request.setPaymentCard(paymentCard);

        // D) Buyer information, derive name and surname
        String fullName = cardDTO.getCardHolderName() != null ? cardDTO.getCardHolderName().trim() : "";
        String buyerName = fullName;
        String buyerSurname = "";
        if (fullName.contains(" ")) {
            String[] parts = fullName.split(" ");
            buyerName = parts[0];
            buyerSurname = parts[parts.length - 1];
        }
        String nowFormatted = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(new Date());

        Buyer buyer = new Buyer();
        buyer.setId("SANTRAL_BUYER");
        buyer.setName(buyerName);
        buyer.setSurname(buyerSurname);
        buyer.setGsmNumber("+905350000000");
        buyer.setEmail("info@santralist.com");
        buyer.setIdentityNumber("00000000000");
        buyer.setLastLoginDate(nowFormatted);
        buyer.setRegistrationDate(nowFormatted);
        buyer.setRegistrationAddress("Emniyettepe, Kazım Karabekir Cd. No:2/20, 34060 Eyüpsultan/İstanbul");
        buyer.setIp("127.0.0.1");
        buyer.setCity("Istanbul");
        buyer.setCountry("Turkey");
        buyer.setZipCode("34060");
        request.setBuyer(buyer);

        // E) Shipping and billing addresses (use same address for deposit)
        Address address = new Address();
        address.setContactName("Santral İstanbul");
        address.setCity("Istanbul");
        address.setCountry("Turkey");
        address.setAddress("Emniyettepe, Kazım Karabekir Cd. No:2/20, 34060 Eyüpsultan/İstanbul");
        address.setZipCode("34060");
        request.setShippingAddress(address);
        request.setBillingAddress(address);

        // F) Basket items (single virtual item for deposit)
        List<BasketItem> basketItems = new ArrayList<>();
        BasketItem basketItem = new BasketItem();
        basketItem.setId("DEP_ITEM");
        basketItem.setName("Para Yatırma");
        basketItem.setCategory1("Para Yatırma");
        basketItem.setCategory2("Deposit");
        basketItem.setItemType(BasketItemType.VIRTUAL.name());
        basketItem.setPrice(dto.getPaidPrice());
        basketItems.add(basketItem);
        request.setBasketItems(basketItems);

        logger.info("mapToCreatePaymentRequest() - End, request created.");
        return request;
    }
}
