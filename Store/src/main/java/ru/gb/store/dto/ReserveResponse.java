package ru.gb.store.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReserveResponse {
    private boolean success;
    private String message;
    private int price;
}
