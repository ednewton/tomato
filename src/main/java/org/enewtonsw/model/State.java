package org.enewtonsw.model;

public final class State {
    public static final State WORKING = new State();
    public static final State BREAKING = new State();
    public static final State SNOOZING = new State();
    public static final State IDLE = new State();
    public static final State ALARMING = new State();

    private State() {
        // prevent instantiation
    }
}
