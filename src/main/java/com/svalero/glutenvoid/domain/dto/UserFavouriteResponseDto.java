package com.svalero.glutenvoid.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserFavouriteResponseDto {

    private Long id;

    private Long userId;
    private String username;

    private Long recipeId;
    private String recipeName;

    private Long establishmentId;
    private String establishmentName;




}
