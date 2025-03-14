//package Bilgi.TDM;
//
//import com.iyzipay.Options;
//import com.iyzipay.model.Payment;
//import com.iyzipay.model.PaymentCard;
//import com.iyzipay.model.Buyer;
//import com.iyzipay.model.Address;
//import com.iyzipay.model.BasketItem;
//import com.iyzipay.model.BasketItemType;
//import com.iyzipay.request.CreatePaymentRequest;
//import org.junit.jupiter.api.Test;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class PaymentIntegrationTest {
//
//    @Test
//    public void testCreatePayment() {
//        // İyzico API ayarlarını yapıyoruz.
//        Options options = new Options();
//        options.setApiKey("your-api-key");            // Kendi API key'inizi girin.
//        options.setSecretKey("your-secret-key");        // Kendi secret key'inizi girin.
//        options.setBaseUrl("https://sandbox-api.iyzipay.com");
//
//        // Ödeme isteği oluşturuluyor.
//        CreatePaymentRequest request = new CreatePaymentRequest();
//        request.setLocale("tr");
//        request.setConversationId("123456789");
//        request.setPrice(new BigDecimal("100.0"));
//        request.setPaidPrice(new BigDecimal("110.0"));
//        request.setCurrency("TRY");
//        request.setInstallment(1);
//        request.setBasketId("B67832");
//        request.setPaymentChannel("WEB");
//        request.setPaymentGroup("PRODUCT");
//
//        // Ödeme kartı bilgilerini ekliyoruz (zorunlu alan).
//        PaymentCard paymentCard = new PaymentCard();
//        paymentCard.setCardHolderName("John Doe");
//        paymentCard.setCardNumber("5528790000000008");
//        paymentCard.setExpireMonth("12");
//        paymentCard.setExpireYear("2030");
//        paymentCard.setCvc("123");
//        paymentCard.setRegisterCard(0);
//        request.setPaymentCard(paymentCard);
//
//        // Alıcı bilgilerini ekliyoruz.
//        Buyer buyer = new Buyer();
//        buyer.setId("BY789");
//        buyer.setName("John");
//        buyer.setSurname("Doe");
//        buyer.setGsmNumber("+905350000000");
//        buyer.setEmail("john.doe@example.com");
//        buyer.setIdentityNumber("74300864791");
//        buyer.setLastLoginDate("2015-10-05 12:43:35");
//        buyer.setRegistrationDate("2013-04-21 15:12:09");
//        buyer.setRegistrationAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
//        buyer.setIp("85.34.78.112");
//        buyer.setCity("Istanbul");
//        buyer.setCountry("Turkey");
//        buyer.setZipCode("34732");
//        request.setBuyer(buyer);
//
//        // Teslimat adresini ekliyoruz.
//        Address shippingAddress = new Address();
//        shippingAddress.setContactName("Jane Doe");
//        shippingAddress.setCity("Istanbul");
//        shippingAddress.setCountry("Turkey");
//        shippingAddress.setAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
//        shippingAddress.setZipCode("34742");
//        request.setShippingAddress(shippingAddress);
//
//        // Fatura adresini ekliyoruz.
//        Address billingAddress = new Address();
//        billingAddress.setContactName("Jane Doe");
//        billingAddress.setCity("Istanbul");
//        billingAddress.setCountry("Turkey");
//        billingAddress.setAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
//        billingAddress.setZipCode("34742");
//        request.setBillingAddress(billingAddress);
//
//        // Sepet elemanlarını (basket items) oluşturuyoruz.
//        List<BasketItem> basketItems = new ArrayList<>();
//
//        BasketItem item1 = new BasketItem();
//        item1.setId("BI101");
//        item1.setName("Binocular");
//        item1.setCategory1("Collectibles");
//        item1.setCategory2("Accessories");
//        item1.setItemType(BasketItemType.PHYSICAL.name());
//        item1.setPrice(new BigDecimal("0.3"));
//        basketItems.add(item1);
//
//        BasketItem item2 = new BasketItem();
//        item2.setId("BI102");
//        item2.setName("Game code");
//        item2.setCategory1("Game");
//        item2.setCategory2("Online Game Items");
//        item2.setItemType(BasketItemType.VIRTUAL.name());
//        item2.setPrice(new BigDecimal("0.5"));
//        basketItems.add(item2);
//
//        BasketItem item3 = new BasketItem();
//        item3.setId("BI103");
//        item3.setName("Usb");
//        item3.setCategory1("Electronics");
//        item3.setCategory2("Usb / Cable");
//        item3.setItemType(BasketItemType.PHYSICAL.name());
//        item3.setPrice(new BigDecimal("0.2"));
//        basketItems.add(item3);
//
//        request.setBasketItems(basketItems);
//
//        // Ödeme işlemini başlatıyoruz.
//        Payment payment = Payment.create(request, options);
//
//        // Dönüş değerini kontrol ediyoruz.
//        assertNotNull(payment, "Payment nesnesi null olmamalıdır");
//        assertEquals("success", payment.getStatus(), "Ödeme işleminin durumu 'success' olmalıdır");
//
//        System.out.println("Payment created successfully with conversationId: " + payment.getConversationId());
//    }
//}
