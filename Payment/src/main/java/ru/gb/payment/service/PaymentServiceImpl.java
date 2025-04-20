package ru.gb.payment.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gb.payment.dto.ActionResponse;
import ru.gb.payment.dto.PaymentRequest;
import ru.gb.payment.model.Account;
import ru.gb.payment.repository.AccountRepository;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final AccountRepository accountRepository;
    private final WebClient webClient;

    @Value("${reserve.url}")
    private String reserveUrl;

    public PaymentServiceImpl(AccountRepository accountRepository, WebClient webClient) {
        this.accountRepository = accountRepository;
        this.webClient = webClient;
    }

    @Override
    @Transactional
    public ActionResponse processPayment(PaymentRequest request) {
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Аккаунт не найден"));

        if (account.getBalance() < request.getAmount()) {
            // Отменить резерв
            webClient.post()
                    .uri(reserveUrl + "/cancel")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

            return new ActionResponse(false, "Недостаточно средств");
        }

        account.setBalance(account.getBalance() - request.getAmount());

        // Подтверждаем резерв
        var commitResponse = webClient.post()
                .uri(reserveUrl + "/commit")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ActionResponse.class)
                .block();

        if (commitResponse == null || !commitResponse.isSuccess()) {
            return new ActionResponse(false, "Не удалось подтвердить резерв на складе");
        }

        return new ActionResponse(true, "Оплата прошла успешно");
    }
}
