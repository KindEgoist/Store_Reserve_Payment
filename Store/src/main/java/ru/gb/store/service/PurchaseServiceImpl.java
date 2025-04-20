package ru.gb.store.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gb.store.dto.*;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final WebClient webClient;

    @Value("${reserve.url}")
    private String reserveUrl;

    @Value("${payment.url}")
    private String paymentUrl;

    public PurchaseServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public PurchaseResponse processPurchase(PurchaseRequest request) {

        //резерв
        ReserveResponse reserveResponse = webClient.post()
                .uri(reserveUrl + "/reserve")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ReserveResponse.class)
                .block();

        if (reserveResponse == null || !reserveResponse.isSuccess()) {
            return new PurchaseResponse(false, "Резервирование не удалось: " +
                    (reserveResponse != null ? reserveResponse.getMessage() : "Нет ответа от сервиса"));
        }

        int totalAmount = reserveResponse.getPrice() * request.getQuantity();

        //оплата
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAccountId(request.getAccountId());
        paymentRequest.setAmount(totalAmount);
        paymentRequest.setProductId(request.getProductId());
        paymentRequest.setQuantity(request.getQuantity());

        ActionResponse paymentResponse = webClient.post()
                .uri(paymentUrl + "/pay")
                .bodyValue(paymentRequest)
                .retrieve()
                .bodyToMono(ActionResponse.class)
                .block();

        if (paymentResponse == null || !paymentResponse.isSuccess()) {

            // Откат резерва
            webClient.post()
                    .uri(reserveUrl + "/cancel")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

            return new PurchaseResponse(false, "Оплата не удалась: " +
                    (paymentResponse != null ? paymentResponse.getMessage() : "Нет ответа от сервиса"));
        }

        // Подтверждение
        ActionResponse commitResponse = webClient.post()
                .uri(reserveUrl + "/commit")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ActionResponse.class)
                .block();

        if (commitResponse == null || !commitResponse.isSuccess()) {
            return new PurchaseResponse(false, "Ошибка при подтверждении резерва: " +
                    (commitResponse != null ? commitResponse.getMessage() : "Нет ответа"));
        }

        return new PurchaseResponse(true, "Покупка успешно завершена!");
    }
}
