package org.enewtonsw.model;

public class Model {
    private String password;
    private int breakCount;

    public Model() {
        password = "password"; //just set a default password.
    }

    public int getBreakCount() {
        return breakCount;
    }

    public void setBreakCount(int breakCount) {
        this.breakCount = breakCount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pass) {
        password = pass;
    }
}
