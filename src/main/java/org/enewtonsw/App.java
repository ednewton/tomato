package org.enewtonsw;

import org.enewtonsw.model.Model;
import org.enewtonsw.presenter.Presenter;
import org.enewtonsw.view.View;

import javax.swing.*;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            View view = new View();
            view.setPresenter(new Presenter(view, new Model()));
        });
    }
}
