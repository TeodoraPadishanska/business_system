package com.example.bussinessSystem.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class OrderedItem {

    @ManyToOne
    private Product product;
    private int quantity;

}
