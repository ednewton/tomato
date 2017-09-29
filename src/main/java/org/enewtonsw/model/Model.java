package org.enewtonsw.model;

public class Model {
    private String password;

    public Model() {
        password = "password"; //just set a default password.
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pass) {
        password = pass;
    }
}
