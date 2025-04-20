package ru.gb.store.service;

import ru.gb.store.dto.PurchaseRequest;
import ru.gb.store.dto.PurchaseResponse;

public interface PurchaseService {
    PurchaseResponse processPurchase(PurchaseRequest request);
}
