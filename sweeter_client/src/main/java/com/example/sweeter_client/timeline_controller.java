package com.example.sweeter_client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Set;

import static com.example.sweeter_client.profileComponent.IsBlocked;
import static com.example.sweeter_client.profileComponent.getUser;
import static com.example.sweeter_client.signin_Controller.toStringArray;
import static java.lang.System.in;
import com.example.sweeter_client.Like;

public class timeline_controller implements Initializable {
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
    private Button MoreButton;
    @FXML
    private ScrollPane tweetScrollPane;

    private VBox tweetVbox;

    private static ArrayList <Tweet> tweets;
    private int ind;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setImageToButton(prof_button, "src/main/resources/com/example/sweeter_client/pictures/user.png");
        setImageToButton(direct_button, "src/main/resources/com/example/sweeter_client/pictures/direct.png");
        setImageToButton(search_button, "src/main/resources/com/example/sweeter_client/pictures/search.png");
        setImageToButton(timeline_button, "src/main/resources/com/example/sweeter_client/pictures/timeline.png");
        setImageToButton(tweet_button, "src/main/resources/com/example/sweeter_client/pictures/tweet.png");
        tweetVbox = new VBox();
        tweetVbox.setSpacing(50);
        tweetScrollPane.setContent(tweetVbox);
        tweetVbox.setStyle("-fx-background-color: #000066;");
        tweetScrollPane.setStyle("-fx-border-color: #192841;" + "-fx-background: #192841;" + "track-background-color: #192841;" + "-fx-focus-color: transparent;" + "-fx-faint-focus-color: transparent;");

        tweets = new ArrayList<>();
        try {
            tweets = getAllTweets();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ind = tweets.size() - 1;
        Load();
    }
    public void Load() {
        int cnt = 0;
        for (int i = ind; i >= 0; i--, ind--) {
            if (cnt == 5)
                break;
            Tweet t = tweets.get(i);
            User usr = new User();
            User usr1 = new User();
            try {
                usr = getUser(t.getWriterId());
                usr1 = getUser(t.getOwnerId());
                if (t.getQuoteTweetId().split("@").length == 2 && t.getQuoteTweetId().split("@")[0].equals("R"))
                    continue;
                if (IsBlocked(usr1, HelloApplication.loggedin_user))
                    continue;

                if (IsFollowing(HelloApplication.loggedin_user, usr) || HelloApplication.loggedin_user.getId().equals(t.getWriterId()) || NumberOfLikes(t.getId()) >= 10) {
                    tweetVbox.getChildren().add(new tweetComponent(t));
                    cnt++;
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public int NumberOfLikes(String tweetId) throws IOException {
        ArrayList <Like> likes = getAllLikes();
        int res = 0;
        for (Like l: likes) {
            if (l.getLiked().equals(tweetId))
                res++;
        }
        return res;
    }
    public ArrayList <Like> getAllLikes() throws IOException {
        String response;
        URL url = new URL("http://localhost:8080/like");
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
        JSONArray jsonObject = new JSONArray(response);
        String[] users = toStringArray(jsonObject);
        boolean Email_existed = false;
        ArrayList <Like> likes = new ArrayList<>();
        for (String t: users) {
            JSONObject obj = new JSONObject(t);
            Like l = new Like(obj.getString("liker"), obj.getString("liked"));
            likes.add(l);
        }
        return likes;
    }
    public boolean IsFollowing(User follower, User following) throws IOException {
        try {
            String response;
            URL url = new URL("http://localhost:8080/follows");
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

            JSONArray jsonObject = new JSONArray(response);
            String[] follows = toStringArray(jsonObject);
            for (String t: follows) {
                JSONObject obj = new JSONObject(t);
                Follow f = new Follow(obj.getString("follower"), obj.getString("followed"));
                if (f.getFollower().equals(follower.getId()) && f.getFollowed().equals(following.getId()))
                    return true;
            }
            return false;
        }
        catch (ConnectException e) {
            System.out.println("Connection failed");
            return false;
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
        m.changeScene(2);
    }

    public void Back_button_Entered() {
        back_button.setStyle("-fx-background-color: #9136FF;");
    }

    public void Back_button_exit() {
        back_button.setStyle("-fx-background-color: #192841;");
    }

    public void More_button_Entered() {
        MoreButton.setStyle("-fx-background-color: #9136FF;");
    }

    public void More_button_exit() {
        MoreButton.setStyle("-fx-background-color: #192841;");
    }

    public void Clear() {

    }

    public static ArrayList<Tweet> getAllTweets() throws IOException {
        ArrayList<Tweet> res = new ArrayList<>();
        try {
            HelloApplication m = new HelloApplication();
            URL url = new URL("http://localhost:8080/tweets");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
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
            JSONArray obj = new JSONArray(response);
            String[] tweets = toStringArray(obj);
            for (String tt : tweets) {
                JSONObject jsonObject = new JSONObject(tt);
                Tweet tweet = new Tweet(jsonObject.getString("id"), jsonObject.getString("writerId"), jsonObject.getString("ownerId"), jsonObject.getString("text"), jsonObject.getString("quoteTweetId"), new Date(jsonObject.getLong("createdAt")), jsonObject.getInt("likes"), jsonObject.getInt("retweets"), jsonObject.getInt("replies"));
                String[] t = toStringArray(jsonObject.getJSONArray("mediaPaths"));
                for (String x : t)
                    tweet.getMediaPaths().add(x);

                res.add(tweet);
            }
        }
        catch (ConnectException e) {
            return res;
        }
        return res;
    }
}