package ru.gb.reserve.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; //имя продукта
    private int available; // количество
    private int price; //цуна на штуку
    private int reserved; // ресерв

}
