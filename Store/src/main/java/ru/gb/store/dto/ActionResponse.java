package ru.gb.store.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActionResponse {
    private boolean success;
    private String message;
}
