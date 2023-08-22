package com.example.sweeter_client;

import com.jfoenix.controls.JFXScrollPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Set;

import static com.example.sweeter_client.edit_profile_controller.gettingBio;
import static com.example.sweeter_client.profileComponent.IsBlocked;
import static com.example.sweeter_client.profileComponent.getUser;
import static com.example.sweeter_client.signin_Controller.toStringArray;
import static com.example.sweeter_client.tweet_controller.getTweet;

public class search_controller implements Initializable {
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
    private TextField searchTextField;

    @FXML
    private Button usersearch_button;
    @FXML
    private ScrollPane scrolpane;
    private VBox vbox;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setImageToButton(prof_button, "src/main/resources/com/example/sweeter_client/pictures/user.png");
        setImageToButton(direct_button, "src/main/resources/com/example/sweeter_client/pictures/direct.png");
        setImageToButton(search_button, "src/main/resources/com/example/sweeter_client/pictures/search.png");
        setImageToButton(timeline_button, "src/main/resources/com/example/sweeter_client/pictures/timeline.png");
        setImageToButton(tweet_button, "src/main/resources/com/example/sweeter_client/pictures/tweet.png");
        setImageToButtonOr(usersearch_button, "src/main/resources/com/example/sweeter_client/pictures/search.png");

        vbox = new VBox();
        vbox.setSpacing(10);
        scrolpane.setContent(vbox);
        vbox.setStyle("-fx-background-color: #9cc3d5FF;");
        scrolpane.setStyle("-fx-border-color: #192841;" + "-fx-background: #192841;" + "track-background-color: #192841;");
    }

    public void searching(ActionEvent event) throws IOException {
        String clue = searchTextField.getText();
        if (clue.length() == 0 || clue.charAt(0) != '#') {
            clue.toLowerCase();
            URL url = new URL("http://localhost:8080/users");
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
            String response = response1.toString();
            JSONArray jsonObject = new JSONArray(response);
            String[] users = toStringArray(jsonObject);

            ArrayList<User> userss = new ArrayList<>();
            for (String t : users) {
                JSONObject obj = new JSONObject(t);
                User user = new User(obj.getString("id"), obj.getString("firstName"), obj.getString("lastName"), obj.getString("email"), obj.getString("phoneNumber"), obj.getString("password"), obj.getString("country"), null);
                if (user.getId().toLowerCase().contains(clue) || user.getFirstName().toLowerCase().contains(clue) || user.getLastName().toLowerCase().contains(clue))
                    userss.add(user);
            }
            vbox = new VBox();
            vbox.setSpacing(50);
            scrolpane.setContent(vbox);
            vbox.setStyle("-fx-background-color: #000066;");
            scrolpane.setStyle("-fx-border-color: #192841;" + "-fx-background: #192841;" + "track-background-color: #192841;");

            for (User usr : userss) {
                Bio bio = null;
                try {
                    if (IsBlocked(usr, HelloApplication.loggedin_user))
                        continue;
                    bio = gettingBio(usr);
                    if (bio == null)
                        bio = new Bio(usr.getId(), "", "", "");
                    profileComponent p = new profileComponent(usr, bio);
                    vbox.getChildren().add(p);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        else {
            String x = searchTextField.getText().substring(1);
            URL url = new URL("http://localhost:8080/hashtag/" + x);
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
            String response = response1.toString();
            String[] tweets = response.split(",");

            vbox = new VBox();
            vbox.setSpacing(50);
            scrolpane.setContent(vbox);
            vbox.setStyle("-fx-background-color: #000066;");
            scrolpane.setStyle("-fx-border-color: #192841;" + "-fx-background: #192841;" + "track-background-color: #192841;");
            if (response.equals(""))
                return;

            for (String tweetID : tweets) {
                Tweet t = getTweet(tweetID);
                if (!IsBlocked(getUser(t.getOwnerId()), HelloApplication.loggedin_user))
                    vbox.getChildren().add(new tweetComponent(t));
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
    public void setImageToButtonOr(Button b, String path) {
        Image image = new Image(Path.of(path).toUri().toString());
        ImageView view = new ImageView(image);
        view.setFitHeight(b.getPrefHeight() - 5);
        view.setFitWidth(b.getPrefWidth() - 5);
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
    public void Back_button_Entered() {
        back_button.setStyle("-fx-background-color: #9136FF;");
    }
    public void Back_button_exit() {
        back_button.setStyle("-fx-background-color: #192841;");
    }

    public void Clear() {

    }
}
