package com.svalero.glutenvoid.domain.heritages;

import com.svalero.glutenvoid.domain.Establishment;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("ESTABLISHMENT")
public class EstablishmentFavourite extends UserFavourite {
    @ManyToOne
    @JoinColumn(name = "establishment_id")
    private Establishment establishment;
}
