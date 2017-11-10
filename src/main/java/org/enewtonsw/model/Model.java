package org.enewtonsw.model;

public class Model {
    private int breakCount;
    private String version;

    public Model() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getBreakCount() {
        return breakCount;
    }

    public void setBreakCount(int breakCount) {
        this.breakCount = breakCount;
    }
}
