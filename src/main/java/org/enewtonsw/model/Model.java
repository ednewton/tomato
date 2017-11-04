package org.enewtonsw.model;

import java.io.IOException;
import java.util.Properties;

public class Model {
    private int breakCount;
    private String version;

    public Model() {
        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getResourceAsStream("/tomato.properties"));
            version = properties.getProperty("version");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getVersion() {
        return version;
    }

    public int getBreakCount() {
        return breakCount;
    }

    public void setBreakCount(int breakCount) {
        this.breakCount = breakCount;
    }
}
