package com.hhuneau.asobi.mtg;

public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException(String reason) {
        super(reason);
    }
}
