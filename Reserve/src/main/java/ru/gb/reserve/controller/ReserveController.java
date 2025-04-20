package ru.gb.reserve.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.reserve.dto.ActionResponse;
import ru.gb.reserve.dto.ReserveResponse;
import ru.gb.reserve.model.Product;
import ru.gb.reserve.service.ReserveService;

@RestController
@RequestMapping("/reserve")
public class ReserveController {

    private final ReserveService reserveService;

    public ReserveController(ReserveService reserveService) {
        this.reserveService = reserveService;
    }

    @PostMapping("/reserve")
    public ResponseEntity<ReserveResponse> reserveProduct(@RequestParam Long productId, @RequestParam int quantity) {

        boolean reserved = reserveService.reserve(productId, quantity);
        if (reserved) {
            Product product = reserveService.getProduct(productId);
            ReserveResponse response =
                    new ReserveResponse(true, "Продукт зарезервирован", product.getPrice());
            return ResponseEntity.ok(response);
        } else {
            ReserveResponse response =
                    new ReserveResponse(false, "Продукт не зарезервирован", 0);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/commit")
    public ResponseEntity<ActionResponse> commitReserve(@RequestParam Long productId, @RequestParam int quantity) {

        boolean committed = reserveService.commitReserve(productId, quantity);
        String msg = committed ? "Продукт продан" : "Недостаточно товара в резерве";
        return ResponseEntity.ok(new ActionResponse(committed, msg));
    }


    @PostMapping("/cancel")
    public ResponseEntity<ActionResponse> cancelReserve(@RequestParam Long productId, @RequestParam int quantity) {
        try {
            reserveService.cancelReserve(productId, quantity);
            return ResponseEntity.ok(new ActionResponse(true, "Резерв отменён"));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(new ActionResponse(false, "Ошибка при отмене: " + e.getMessage()));
        }
    }

}
