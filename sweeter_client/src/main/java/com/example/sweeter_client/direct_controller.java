package com.example.sweeter_client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.example.sweeter_client.DirectView_controller.getAllMessage;
import static com.example.sweeter_client.signin_Controller.toStringArray;
import static java.lang.System.in;

public class direct_controller implements Initializable {
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
    private ScrollPane notifScrollPane;
    VBox messageVBox;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setImageToButton(prof_button, "src/main/resources/com/example/sweeter_client/pictures/user.png");
        setImageToButton(direct_button, "src/main/resources/com/example/sweeter_client/pictures/direct.png");
        setImageToButton(search_button, "src/main/resources/com/example/sweeter_client/pictures/search.png");
        setImageToButton(timeline_button, "src/main/resources/com/example/sweeter_client/pictures/timeline.png");
        setImageToButton(tweet_button, "src/main/resources/com/example/sweeter_client/pictures/tweet.png");

        messageVBox = new VBox();
        messageVBox.setSpacing(10);
        messageVBox.setStyle("-fx-background-color: #192841;");
        notifScrollPane.setContent(messageVBox);
        notifScrollPane.setStyle("-fx-border-color: #192841;" + "-fx-background: #192841;" + "track-background-color: #192841;" + "-fx-focus-color: transparent;" + "-fx-faint-focus-color: transparent;");

            ArrayList<Message> messages = new ArrayList<>();
            try {
                messages = getAllRec(HelloApplication.loggedin_user);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            for (Message m: messages) {
                try {
                    messageVBox.getChildren().add(new notifComponent(m));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
    }

    public void setImageToButton(Button b, String path) {
        Image image = new Image(Path.of(path).toUri().toString());
        ImageView view = new ImageView(image);
        view.setFitHeight(b.getPrefHeight() - 50);
        view.setFitWidth(b.getPrefWidth() - 50);
        b.setGraphic(view);
    }

    public ArrayList <Message> getAllRec(User usr) throws IOException {
        ArrayList <Message> res = new ArrayList<>();
        URL url = new URL("http://localhost:8080/direct/" + usr.getId());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("JWT", HelloApplication.token);
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        BufferedReader in1 = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputline1;
        StringBuffer response2 = new StringBuffer();
        while ((inputline1 = in1.readLine()) != null) {
            response2.append(inputline1);
        }
        in.close();
        String response = response2.toString();

        System.out.println(response);

        JSONArray obj = new JSONArray(response);
        String[] messages = toStringArray(obj);

        for (String tt : messages) {
            JSONObject jsonObject = new JSONObject(tt);
            Message m = new Message(jsonObject.getString("id"), jsonObject.getString("sender"), jsonObject.getString("receiver"), jsonObject.getString("text"), jsonObject.getLong("createdAt"));
            res.add(m);
        }

        return res;
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
    public void Back_button_Entered() {
        back_button.setStyle("-fx-background-color: #9136FF;");
    }
    public void Back_button_exit() {
        back_button.setStyle("-fx-background-color: #192841;");
    }

    public void Clear() {

    }
}
