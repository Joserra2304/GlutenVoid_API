package com.svalero.glutenvoid.domain.dto;

import com.svalero.glutenvoid.domain.entity.Establishment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstablishmentDto {
    private long id;
    private String name;
    private String description;
    private String address;
    private String city;
    private int phoneNumber;
    private double latitude;
    private double longitude;
    private boolean glutenFreeOption;

    public EstablishmentDto(Establishment establishment) {
        this.id = establishment.getId();
        this.name = establishment.getName();
        this.description = establishment.getDescription();
        this.address = establishment.getAddress();
        this.city = establishment.getCity();
        this.phoneNumber = establishment.getPhoneNumber();
        this.latitude = establishment.getLatitude();
        this.longitude = establishment.getLongitude();
        this.glutenFreeOption = establishment.isGlutenFreeOption();
        // userId no se incluye para evitar exponer información del usuario
    }
}
