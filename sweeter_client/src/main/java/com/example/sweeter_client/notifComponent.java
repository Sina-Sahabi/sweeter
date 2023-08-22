package com.example.sweeter_client;

import com.jfoenix.controls.JFXTextArea;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.sweeter_client.profileComponent.getAvatar;
import static com.example.sweeter_client.profileComponent.getUser;

public class notifComponent extends AnchorPane {
    Label notifTextLabel;
    Label dateLabel;
    Message message;
    public notifComponent(Message message) throws IOException {
        this.message = message;

        notifTextLabel = new Label(message.getSender() + " send you a message!");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateLabel = new Label(simpleDateFormat.format(new Date(message.getCreatedAt())));

        setConfig();
        setAction();
        setLocation();

        this.getChildren().addAll(notifTextLabel, dateLabel);
    }
    private void setConfig() throws IOException {
        notifTextLabel.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 25));
        notifTextLabel.setTextFill(Paint.valueOf("gray"));

        notifTextLabel.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 15));
        notifTextLabel.setTextFill(Paint.valueOf("white"));
    }
    private void setLocation() throws IOException {
        this.setPrefSize(580, 70);
        this.setStyle("-fx-background-color: #192841");

        AnchorPane.setLeftAnchor(notifTextLabel, 20.0);
        AnchorPane.setRightAnchor(dateLabel, 20.0);
    }
    private void setAction() throws IOException {
        notifTextLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                HelloApplication m = new HelloApplication();
                try {
                    DirectView_controller.user = getUser(message.getSender());
                    m.changeScene(11);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
