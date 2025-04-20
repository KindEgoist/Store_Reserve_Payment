package ru.gb.store.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.store.dto.PurchaseRequest;
import ru.gb.store.dto.PurchaseResponse;
import ru.gb.store.service.PurchaseService;

@RestController
@RequestMapping("/store")
public class StoreController {

    private final PurchaseService purchaseService;

    public StoreController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping("/purchase")
    public ResponseEntity<PurchaseResponse> makePurchase(@RequestBody PurchaseRequest request) {
        PurchaseResponse response = purchaseService.processPurchase(request);
        return ResponseEntity.ok(response);
    }
}
