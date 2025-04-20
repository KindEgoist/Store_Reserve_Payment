package ru.gb.reserve.service;

import ru.gb.reserve.model.Product;

public interface ReserveService {
    boolean reserve(Long productId, int quantity);
    boolean commitReserve(Long productId, int quantity);
    void cancelReserve(Long productId, int quantity);
    public Product getProduct(Long id);
}
