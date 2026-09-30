package com.example.bussinessSystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateRequest {

    private String email;
    private String firstName;
    private String lastName;
    private String company = "";
    private String address;
    private String city;
    private String country;
    private String postalCode;
    private String phoneNumber;

    private List<OrderedItemsReq> items;
}
