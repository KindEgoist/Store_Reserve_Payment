package ru.gb.payment.service;

import ru.gb.payment.dto.ActionResponse;
import ru.gb.payment.dto.PaymentRequest;

public interface PaymentService {
    ActionResponse processPayment(PaymentRequest request);
}
