package com.svalero.glutenvoid.repository;

import com.svalero.glutenvoid.domain.Recipe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    List<Recipe> findAll();

    List<Recipe> findByName(String name);

    List<Recipe> findByPreparationTime(int time);

}
