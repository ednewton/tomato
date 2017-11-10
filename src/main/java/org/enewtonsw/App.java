package org.enewtonsw;

import org.enewtonsw.model.Model;
import org.enewtonsw.presenter.Presenter;
import org.enewtonsw.view.View;
import org.enewtonsw.view.impl.SwingImpl;

import javax.swing.*;
import java.io.IOException;
import java.util.Properties;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        App app = new App();
        app.run(args);
    }

    public void run(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Model model = new Model();
            try {
                Properties properties = new Properties();
                properties.load(this.getClass().getResourceAsStream("/filtered/tomato.properties"));
                model.setVersion(properties.getProperty("version"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            View view = new SwingImpl();
            Presenter presenter = new Presenter(view, model);
            view.setPresenter(presenter);
        });
    }
}
