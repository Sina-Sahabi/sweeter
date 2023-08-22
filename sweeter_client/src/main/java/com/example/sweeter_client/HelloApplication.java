package com.example.sweeter_client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class HelloApplication extends Application {
    private static Stage stg;
    private static Scene MainMenu;
    private static Scene SignIn;
    public static User loggedin_user = new User("mahdi", "mahdi", "haeri", "mahdihaerim@gmail.com", "123123123", "123", "Iran", new Date());
    public static String token = "mahdi!@QZV==";
    @Override
    public void start(Stage stage) throws IOException {
        stg = stage;
        stage.setResizable(false);

        MainMenu = new Scene((new FXMLLoader(HelloApplication.class.getResource("MainMenu.fxml"))).load());
        SignIn = new Scene((new FXMLLoader(HelloApplication.class.getResource("SigninMenu.fxml"))).load());
        stage.setTitle("Sweeter");
        stg.setScene(MainMenu);
        stage.show();

    }
    public void changeScene(int x) throws IOException {
        if (x == 1)
            stg.setScene(MainMenu);
        else if (x == 2)
            stg.setScene(new Scene((new FXMLLoader(HelloApplication.class.getResource("forUpage.fxml"))).load()));
        else if (x == 3)
            stg.setScene(SignIn);
        else if (x == 4)
            stg.setScene(new Scene((new FXMLLoader(HelloApplication.class.getResource("Timeline.fxml"))).load()));
        else if (x == 5)
            stg.setScene(new Scene((new FXMLLoader(HelloApplication.class.getResource("tweet.fxml"))).load()));
        else if (x == 6)
            stg.setScene(new Scene((new FXMLLoader(HelloApplication.class.getResource("search.fxml"))).load()));
        else if (x == 7)
            stg.setScene(new Scene((new FXMLLoader(HelloApplication.class.getResource("direct.fxml"))).load()));
        else if (x == 8)
            stg.setScene(new Scene((new FXMLLoader(HelloApplication.class.getResource("edit_profile.fxml"))).load()));
        else if (x == 9)
            stg.setScene(new Scene((new FXMLLoader(HelloApplication.class.getResource("UserView.fxml"))).load()));
        else if (x == 10)
            stg.setScene(new Scene((new FXMLLoader(HelloApplication.class.getResource("TweetView.fxml"))).load()));
        else if (x == 11)
            stg.setScene(new Scene((new FXMLLoader(HelloApplication.class.getResource("DirectView.fxml"))).load()));
        else if (x == 12)
            stg.setScene(new Scene((new FXMLLoader(HelloApplication.class.getResource("trendPage.fxml"))).load()));

    }

    public static void main(String[] args) {
        launch();
    }
}