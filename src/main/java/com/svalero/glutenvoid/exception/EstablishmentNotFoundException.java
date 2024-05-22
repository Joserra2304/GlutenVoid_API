package com.svalero.glutenvoid.exception;

public class EstablishmentNotFoundException extends Exception {

    public EstablishmentNotFoundException() {
        super("Establishment not found");
    }

    public EstablishmentNotFoundException(String message) {
        super(message);
    }
}
