package com.telran.annotation.product.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Product {

    private Integer id;
    private String productName;
    private Double price;
}
