package com.hhuneau.asobi.game;

public enum Status {
    CREATED, STARTED, FINISHED;

    public boolean hasStarted() {
        return this.equals(STARTED) || this.equals(FINISHED);
    }
}
