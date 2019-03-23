package com.faire.orders.faireorders.model;

import com.faire.orders.faireorders.domain.Address;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class AddressLog {
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

    public static AddressLog from(Address address) {
        if (address == null) {
            return null;
        }

        AddressLog t = new AddressLog();
        t.name = address.getName();
        t.address1 = address.getAddress1();
        t.address2 = address.getAddress2();
        t.postalCode = address.getPostalCode();
        t.city = address.getCity();
        t.state = address.getState();
        t.stateCode = address.getStateCode();
        t.phoneNumber = address.getPhoneNumber();
        t.country = address.getCountry();
        t.countryCode = address.getCountryCode();
        t.companyName = address.getCompanyName();

        return t;
    }
}
