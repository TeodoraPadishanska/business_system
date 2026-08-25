package com.example.bussinessSystem.entities;

import com.example.bussinessSystem.enums.MovementType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    private Long quantity;

    @Enumerated(EnumType.STRING)
    private MovementType movementType;

    @ManyToOne
    private User movedBy;

    private LocalDateTime movedAt_time = LocalDateTime.now();


}
