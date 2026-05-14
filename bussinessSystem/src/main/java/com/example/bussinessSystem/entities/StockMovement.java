package com.example.bussinessSystem.entities;

import com.example.bussinessSystem.enums.MovementType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String product;
    private MovementType movementType;
    private String reason;
    private String movedBy;
    private LocalDateTime movedAt_time;


}
