package com.example.bussinessSystem.Dto;

import com.example.bussinessSystem.enums.MovementType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockMovementReq {
    private Long productId;
    private Long quantity;

    @Enumerated(EnumType.STRING)
    private MovementType movementType;

    private Long movedByUserId;

}
