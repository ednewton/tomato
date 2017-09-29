package org.enewtonsw;

import org.enewtonsw.model.Model;
import org.enewtonsw.presenter.Presenter;
import org.enewtonsw.view.impl.SwingImpl;
import org.enewtonsw.view.View;

import javax.swing.*;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            View view = new SwingImpl();
            Presenter presenter = new Presenter(view, new Model());
            view.setPresenter(presenter);
        });
    }
}
