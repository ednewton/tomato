package org.enewtonsw.model;

public final class State {
    public static final State WORKING = new State("Work");
    public static final State BREAKING = new State("Break");
    public static final State IDLE = new State("Idle");
    private final String name;

    private State(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
