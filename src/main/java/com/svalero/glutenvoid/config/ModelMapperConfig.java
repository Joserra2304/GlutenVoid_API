package com.svalero.glutenvoid.config;

import com.svalero.glutenvoid.domain.dto.RecipeDto;
import com.svalero.glutenvoid.domain.entity.Recipe;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Configuración de mapeo personalizado para Recipe a RecipeDto
        modelMapper.addMappings(new PropertyMap<Recipe, RecipeDto>() {
            @Override
            protected void configure() {
                // Asegúrate de manejar los posibles nulos
                map().setUserId(source.getUser() != null ? source.getUser().getId() : null);
                map(source.getUser() != null ? source.getUser().getUsername() : null, destination.getUsername());
            }
        });

        return modelMapper;
    }
}
