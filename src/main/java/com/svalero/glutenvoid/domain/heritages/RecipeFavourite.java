package com.svalero.glutenvoid.domain.heritages;

import com.svalero.glutenvoid.domain.Recipe;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("RECIPE")
public class RecipeFavourite extends UserFavourite {
    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
}
