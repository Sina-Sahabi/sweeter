package com.example.sweeter_client;

import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import static com.example.sweeter_client.profileComponent.getAvatar;
import static com.example.sweeter_client.profileComponent.getUser;
import static com.example.sweeter_client.signin_Controller.toStringArray;

public class SimpletweetComponent extends AnchorPane {
    Label user_idLabel;
    Circle circleClipProfile;
    ImageView AvatarImageView;
    Label nameLabel;
    JFXTextArea TweetBody;
    Tweet tweet;
    Image AvatarImage;
    User user = new User("", "", "", "", "", "", "", new Date());
    public SimpletweetComponent(Tweet tweet) throws IOException {
        this.tweet = tweet;
        this.user = getUser(tweet.getOwnerId());
        AvatarImage = getAvatar(user);
        AvatarImageView = new ImageView(AvatarImage);
        user_idLabel = new Label("@" + user.getId());
        nameLabel = new Label(user.getFirstName());
        circleClipProfile = new Circle(30);
        TweetBody = new JFXTextArea();
        TweetBody.setText(tweet.getText());
        setAction();
        setConfig();
        setLocation();

//        this.getChildren().addAll(user_idLabel, circleClipProfile, nameLabel, TweetBody);
        this.getChildren().addAll(circleClipProfile, nameLabel, user_idLabel, TweetBody);
    }
    private void setConfig() throws IOException {
        this.setPrefSize(540, 250);
        this.setStyle("-fx-background-color: #192841");

        user_idLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
        user_idLabel.setTextFill(Paint.valueOf("gray"));

        circleClipProfile.setFill(new ImagePattern(AvatarImage));

        nameLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        nameLabel.setTextFill(Paint.valueOf("white"));

        TweetBody.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
        TweetBody.setPrefSize(400, 150);
        TweetBody.setStyle("-fx-text-fill: white;" + "-jfx-focus-color: #192841;" + "-jfx-unfocus-color: #192841;") ;
        TweetBody.setEditable(false);
        TweetBody.setWrapText(true);
    }
    private void setLocation() {
        AnchorPane.setTopAnchor(circleClipProfile, 20.0);
        AnchorPane.setLeftAnchor(circleClipProfile, 20.0);

        AnchorPane.setTopAnchor(nameLabel, 20.0);
        AnchorPane.setLeftAnchor(nameLabel, 90.0);

        AnchorPane.setTopAnchor(user_idLabel, 45.0);
        AnchorPane.setLeftAnchor(user_idLabel, 90.0);

        AnchorPane.setTopAnchor(TweetBody, 90.0);
        AnchorPane.setLeftAnchor(TweetBody, 90.0);
    }
    private void setAction() {
        nameLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                UserView_controller.users = new ArrayList<>();
                UserView_controller.users.add(user);
                HelloApplication m = new HelloApplication();
                try {
                    m.changeScene(9);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
