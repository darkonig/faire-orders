package com.faire.orders.faireorders.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Address {
    private String name;
    private String address1;
    private String address2;
    @JsonProperty("postal_code")
    private String postalCode;
    private String city;
    private String state;
    @JsonProperty("state_code")
    private String stateCode;
    @JsonProperty("phone_number")
    private String phoneNumber;
    private String country;
    @JsonProperty("country_code")
    private String countryCode;
    @JsonProperty("company_name")
    private String companyName;
}
