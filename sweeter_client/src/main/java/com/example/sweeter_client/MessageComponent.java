package com.example.sweeter_client;

import com.jfoenix.controls.JFXTextArea;
import javafx.geometry.NodeOrientation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.io.IOException;

import static com.example.sweeter_client.profileComponent.getAvatar;
import static com.example.sweeter_client.profileComponent.getUser;

public class MessageComponent extends AnchorPane {
    Circle circleClipProfile;
    ImageView AvatarImageView;
    JFXTextArea messageBody;
    Image AvatarImage;
    Message message;
    public MessageComponent(Message message) throws IOException {
        this.message = message;
        circleClipProfile = new Circle(30);
        AvatarImage = getAvatar(getUser(message.getSender()));
        AvatarImageView = new ImageView(AvatarImage);
        messageBody = new JFXTextArea();
        messageBody.setText(message.getText());

        setConfig();
        setAction();
        setLocation();

        this.getChildren().add(messageBody);
        this.getChildren().add(circleClipProfile);
    }
    private void setConfig() throws IOException {
        messageBody.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
        messageBody.setPrefSize(400, 100);
        messageBody.setStyle("-fx-text-fill: white;" + "-jfx-focus-color: #4059a9;" + "-jfx-unfocus-color: #4059a9;") ;

        messageBody.setEditable(false);
        messageBody.setWrapText(true);

        circleClipProfile.setFill(new ImagePattern(AvatarImage));
    }
    private void setLocation() throws IOException {
        this.setPrefSize(580, 150);
        this.setStyle("-fx-background-color: #192841");
        AnchorPane.setTopAnchor(circleClipProfile, 20.0);
        if (HelloApplication.loggedin_user.getId().equals(message.getSender())) {
            AnchorPane.setRightAnchor(circleClipProfile, 20.0);
            messageBody.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            AnchorPane.setTopAnchor(messageBody, 20.0);
            AnchorPane.setRightAnchor(messageBody, 100.0);
        }
        else {
            AnchorPane.setLeftAnchor(circleClipProfile, 20.0);
            AnchorPane.setTopAnchor(messageBody, 20.0);
            AnchorPane.setLeftAnchor(messageBody, 100.0);
        }
    }
    private void setAction() throws IOException {

    }
}
