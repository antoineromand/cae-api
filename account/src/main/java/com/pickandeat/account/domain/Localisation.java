package com.pickandeat.account.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Localisation(String address1, String address2,String address3, String city, String postalCode, String country) {
    public String fullAddress() {
        return Stream.of(address1, address2, address3, city, postalCode, country)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> ((String) s).isEmpty())
                .collect(Collectors.joining(", "));
    }
}
