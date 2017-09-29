package org.enewtonsw.model;

public class Model {
    private String password;
    private int breakCounter;

    public Model() {
        password = "password"; //just set a default password.
    }

    public int getBreakCounter() {
        return breakCounter;
    }

    public void setBreakCounter(int breakCounter) {
        this.breakCounter = breakCounter;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pass) {
        password = pass;
    }
}
