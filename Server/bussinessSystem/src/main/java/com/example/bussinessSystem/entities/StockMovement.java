package com.example.bussinessSystem.entities;

import com.example.bussinessSystem.enums.MovementType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String product;

    @Enumerated(EnumType.STRING)
    private MovementType movementType;

    private String reason;
    private String movedBy;
    private LocalDateTime movedAt_time = LocalDateTime.now();


}
