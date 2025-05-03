package com.example.javafxdemog6;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class CountApplication extends Application {
    private int count = 0;
    @Override
    public void start(Stage stage) throws IOException {
        Label label = new Label("Count: 0");
        Button button = new Button("Click me!");
        button.setOnAction(e -> {
            count++;
            label.setText("Count: " + count);
        });
        VBox layout = new VBox(15);
        layout.getChildren().addAll(button, label);

        Scene scene = new Scene(layout, 400, 350);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());


        stage.setTitle("my app!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
