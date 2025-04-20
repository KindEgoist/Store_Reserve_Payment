package ru.gb.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.payment.dto.ActionResponse;
import ru.gb.payment.dto.PaymentRequest;
import ru.gb.payment.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/pay")
    public ResponseEntity<ActionResponse> processPayment(@RequestBody PaymentRequest request) {
        ActionResponse response = paymentService.processPayment(request);
        return ResponseEntity.ok(response);
    }
}
