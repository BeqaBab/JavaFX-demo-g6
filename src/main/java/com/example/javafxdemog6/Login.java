package com.example.javafxdemog6;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.sql.*;
import java.util.Scanner;

public class Login extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public static long hash(String s) {
    final int p = 31;
    final int m = 100000009;
        long hashValue = 0;
        long p_pow = 1;
        for (int i = 0; i < s.length(); i++) {
            hashValue = (hashValue + (s.charAt(i) - 'a' + 1) * p_pow) % m;
            p_pow = (p_pow * p) % m;
        }
        return hashValue;
    }

    public static String Initiate() throws FileNotFoundException {
        File obj = new File("C:\\Users\\beqab\\JavaFX-demo-g6\\src\\main\\java\\com\\example\\javafxdemog6\\Password");
        Scanner myReader = new Scanner(obj);
        String password = "";
        while(myReader.hasNext()){
            password = myReader.nextLine();
        }
        myReader.close();

        return password;
    }

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException, MalformedURLException {
        String url = "jdbc:postgresql://localhost:5432/UserDatabase";
        String user = "postgres";
        String password = Initiate();

        Label userLabel = new Label("Username:");
        Label passLabel = new Label("Password:");

        Label messageLabel = new Label();

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String pass = passwordField.getText();
            try{
                Connection connection = DriverManager.getConnection(url, user, password);
                String insertQuery = "SELECT pass_word FROM users where user_name = ?;";
                PreparedStatement stmt = connection.prepareStatement(insertQuery);
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                while(rs.next()){
                    long pass_word = rs.getLong("pass_word");
                    if(pass_word == (hash(pass))){
                        messageLabel.setText("Logged in successfully!");
                    }   else{
                        messageLabel.setText("Wrong password.");
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        });

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String pass = passwordField.getText();
            try{
                Connection connection = DriverManager.getConnection(url, user, password);
                String insertQuery = "INSERT INTO users(user_name, pass_word) VALUES(?, ?);";
                PreparedStatement stmt = connection.prepareStatement(insertQuery);
                stmt.setString(1, username);
                stmt.setLong(2, hash(pass));
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    messageLabel.setText("User added successfully!");
                } else {
                    messageLabel.setText("User couldn't be added.");
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(userLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passLabel, 0, 1);
        grid.add(passwordField, 1,1);

        grid.add(loginButton, 1, 2);
        grid.add(registerButton, 1, 3);
        grid.add(messageLabel, 1, 4);

        Scene scene = new Scene(grid, 300, 200);
        File cssFile = new File("C:\\Users\\beqab\\JavaFX-demo-g6\\src\\main\\java\\com\\example\\javafxdemog6\\login-style.css");
        scene.getStylesheets().add(cssFile.toURI().toURL().toExternalForm());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
