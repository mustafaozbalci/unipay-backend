package unipay.service;

import unipay.dto.PaymentCardDTO;
import unipay.dto.PaymentDepositRequestDTO;
import com.iyzipay.Options;
import com.iyzipay.model.*;
import com.iyzipay.request.CreatePaymentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class IyzicoPaymentService {

    private static final Logger logger = LoggerFactory.getLogger(IyzicoPaymentService.class);

    private final Options options;

    // Options bean'i Config’den alıyoruz.
    public IyzicoPaymentService(Options options) {
        this.options = options;
    }

    /**
     * 1) price & paidPrice validation
     * 2) CreatePaymentRequest oluştur
     * 3) Iyzico'ya istek gönder
     */
    public Payment depositMoney(PaymentDepositRequestDTO dto) throws Exception {
        logger.info("depositMoney() - Start, depositRequestDTO: {}", dto);

        validatePrices(dto.getPrice(), dto.getPaidPrice());
        CreatePaymentRequest request = mapToCreatePaymentRequest(dto);

        logger.info("Calling Iyzico Payment.create(...)");
        Payment payment = Payment.create(request, options);

        logger.info("Iyzico Payment result: status={}, conversationId={}, errorMessage={}",
                payment.getStatus(), payment.getConversationId(), payment.getErrorMessage());

        logger.info("depositMoney() - End");
        return payment;
    }

    // price ve paidPrice kontrolü
    private void validatePrices(BigDecimal price, BigDecimal paidPrice) {
        logger.info("validatePrices() - Start, price: {}, paidPrice: {}", price, paidPrice);

        if (price == null || paidPrice == null) {
            logger.error("price veya paidPrice null geldi.");
            throw new IllegalArgumentException("price ve paidPrice zorunludur.");
        }
        if (price.compareTo(paidPrice) != 0) {
            logger.error("Gönderilen tutar, tüm kırılımların toplam tutarına eşit değil. Price: {}, PaidPrice: {}",
                    price, paidPrice);
            throw new IllegalArgumentException("Gönderilen tutar, tüm kırılımların toplam tutarına eşit olmalıdır.");
        }

        logger.info("validatePrices() - End, tutarlar geçerli.");
    }

    // PaymentDepositRequestDTO -> CreatePaymentRequest çevirimi
    private CreatePaymentRequest mapToCreatePaymentRequest(PaymentDepositRequestDTO dto) {
        logger.info("mapToCreatePaymentRequest() - Start, dto: {}", dto);

        CreatePaymentRequest request = new CreatePaymentRequest();

        // A) Tutarlar
        request.setPrice(dto.getPrice());
        request.setPaidPrice(dto.getPaidPrice());
        request.setInstallment(1);

        // B) Varsayılanlar
        request.setBasketId("DEP-" + System.currentTimeMillis());
        request.setPaymentChannel("WEB");
        request.setPaymentGroup("PRODUCT");
        request.setLocale("tr");
        request.setConversationId("conv-" + System.currentTimeMillis());
        request.setCurrency("TRY");

        // C) Kart bilgileri
        PaymentCardDTO cardDTO = dto.getPaymentCard();
        if (cardDTO == null) {
            logger.error("PaymentCard bilgisi null geldi. Kart zorunlu!");
            throw new IllegalArgumentException("PaymentCard bilgileri zorunludur.");
        }
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setCardHolderName(cardDTO.getCardHolderName());
        paymentCard.setCardNumber(cardDTO.getCardNumber());
        paymentCard.setExpireMonth(cardDTO.getExpireMonth());
        paymentCard.setExpireYear(cardDTO.getExpireYear());
        paymentCard.setCvc(cardDTO.getCvc());
        paymentCard.setRegisterCard(cardDTO.getRegisterCard());
        request.setPaymentCard(paymentCard);

        // D) Buyer bilgileri: cardHolderName üzerinden ayarlama yapıyoruz.
        String fullName = cardDTO.getCardHolderName() != null ? cardDTO.getCardHolderName().trim() : "";
        String buyerName = fullName;
        String buyerSurname = "";
        if (fullName.contains(" ")) {
            String[] parts = fullName.split(" ");
            buyerName = parts[0];
            buyerSurname = parts[parts.length - 1];
        }

        // Tarih formatı: "yyyy-MM-dd HH:mm:ss"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String nowFormatted = sdf.format(new Date());

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

        // E) Shipping & Billing Adresleri
        Address address = new Address();
        address.setContactName("Santral İstanbul");
        address.setCity("Istanbul");
        address.setCountry("Turkey");
        address.setAddress("Emniyettepe, Kazım Karabekir Cd. No:2/20, 34060 Eyüpsultan/İstanbul");
        address.setZipCode("34060");
        request.setShippingAddress(address);
        request.setBillingAddress(address);

        // F) Sepet (Deposit işlemi için tek ürün)
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
