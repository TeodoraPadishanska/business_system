package com.example.bussinessSystem.Dto;

import com.example.bussinessSystem.enums.OrderStatus;
import com.example.bussinessSystem.enums.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String company = "";

    @NotBlank
    private String address;

    @NotBlank
    private String city;

    @NotBlank
    private String country;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String phoneNumber;

    private LocalDate expectedDelivery;

    @NotNull
    private OrderStatus orderStatus;

    @NotNull
    private PaymentStatus paymentStatus;
}