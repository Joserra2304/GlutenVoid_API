package com.svalero.glutenvoid.repository;

import com.svalero.glutenvoid.domain.entity.Recipe;
import com.svalero.glutenvoid.exception.RecipeNotFoundException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    List<Recipe> findAll();

    List<Recipe> findByApprovedRecipe(Boolean isApproved) throws RecipeNotFoundException;

    List<Recipe> findByUserId(long userId) throws RecipeNotFoundException;

}