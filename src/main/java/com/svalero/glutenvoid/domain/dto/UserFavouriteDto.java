package com.svalero.glutenvoid.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFavouriteDto {

    private Long userId;
    private Long establishmentId;
    private Long recipeId;

}
