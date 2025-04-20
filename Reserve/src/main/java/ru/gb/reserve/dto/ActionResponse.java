package ru.gb.reserve.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ActionResponse {
    private boolean success;
    private String message;
}
