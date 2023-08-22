package com.example.sweeter_client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class prof_controller implements Initializable {
    @FXML
    private Button direct_button;

    @FXML
    private Button prof_button;

    @FXML
    private Button search_button;

    @FXML
    private Button timeline_button;

    @FXML
    private Button tweet_button;
    @FXML
    private Button back_button;
    @FXML
    private Button edit_button;
    @FXML
    private VBox profiles;
    @FXML
    private Button trendButton;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setImageToButton(prof_button, "src/main/resources/com/example/sweeter_client/pictures/user.png");
        setImageToButton(direct_button, "src/main/resources/com/example/sweeter_client/pictures/direct.png");
        setImageToButton(search_button, "src/main/resources/com/example/sweeter_client/pictures/search.png");
        setImageToButton(timeline_button, "src/main/resources/com/example/sweeter_client/pictures/timeline.png");
        setImageToButton(tweet_button, "src/main/resources/com/example/sweeter_client/pictures/tweet.png");

        profiles.setSpacing(10);
        profiles.setStyle("-fx-background-color: #192841");

        Bio bio = null;

        try {
            bio = gettingBio(HelloApplication.loggedin_user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (bio == null) {
            bio = new Bio(HelloApplication.loggedin_user.getId(), "", "", "");
        }

        try {
            profiles.getChildren().add(new profileComponent(HelloApplication.loggedin_user, bio));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void setImageToButton(Button b, String path) {
        Image image = new Image(Path.of(path).toUri().toString());
        ImageView view = new ImageView(image);
        view.setFitHeight(b.getPrefHeight() - 50);
        view.setFitWidth(b.getPrefWidth() - 50);
        b.setGraphic(view);
    }

    public void setProf_button(ActionEvent event) throws Exception {
        HelloApplication m = new HelloApplication();
        Clear();
        m.changeScene(2);
    }
    public void setDirect_button(ActionEvent event) throws Exception {
        HelloApplication m = new HelloApplication();
        Clear();
        m.changeScene(7);
    }
    public void setSearch_button(ActionEvent event) throws Exception {
        HelloApplication m = new HelloApplication();
        Clear();
        m.changeScene(6);
    }
    public void setTimeline_button(ActionEvent event) throws Exception {
        HelloApplication m = new HelloApplication();
        Clear();
        m.changeScene(4);
    }
    public void setTweet_button(ActionEvent event) throws Exception {
        HelloApplication m = new HelloApplication();
        Clear();
        m.changeScene(5);
    }
    public void goBack() throws Exception {
        HelloApplication m = new HelloApplication();
        Clear();
        m.changeScene(1);
    }
    public void setEdit_button(ActionEvent event) throws Exception {
        HelloApplication m = new HelloApplication();
        Clear();
        edit_profile_controller con = new edit_profile_controller();
        m.changeScene(8);
    }
    public void Back_button_Entered() {
        back_button.setStyle("-fx-background-color: #9136FF;");
    }
    public void Back_button_exit() {
        back_button.setStyle("-fx-background-color: #192841;");
    }
    public void edit_button_Entered() {
        edit_button.setStyle("-fx-background-color: #9136FF;");
    }
    public void edit_button_exit() {
        edit_button.setStyle("-fx-background-color: #192841;");
    }

    public void trend_button_Entered() {
        trendButton.setStyle("-fx-background-color: #9136FF;");
    }
    public void trend_button_exit() {
        trendButton.setStyle("-fx-background-color: #192841;");
    }
    public void trendPressed() throws IOException {
        HelloApplication m = new HelloApplication();
        m.changeScene(12);
    }
    public void Clear() {

    }
    public static Bio gettingBio(User user) throws IOException {
        try {
            String response;
            URL url = new URL("http://localhost:8080/bios/" + user.getId());

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputline;
            StringBuffer response1 = new StringBuffer();
            while ((inputline = in.readLine()) != null) {
                response1.append(inputline);
            }
            in.close();
            response = response1.toString();

            if (response.equals("no bio"))
                return new Bio(user.getId(), "", "", "");

            JSONObject jsonObject = new JSONObject(response);
            Bio bio = new Bio(user.getId(), jsonObject.getString("biography"), jsonObject.getString("location"), jsonObject.getString("website"));
            System.out.println(bio);
            return bio;
        }
        catch (ConnectException e) {
            return null;
        }
    }
}
