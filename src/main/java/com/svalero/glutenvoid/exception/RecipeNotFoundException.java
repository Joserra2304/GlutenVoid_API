package com.svalero.glutenvoid.exception;

public class RecipeNotFoundException extends Exception{

    public RecipeNotFoundException() {
        super("Recipe not found");
    }

    public RecipeNotFoundException(String message) {
        super(message);
    }
}
