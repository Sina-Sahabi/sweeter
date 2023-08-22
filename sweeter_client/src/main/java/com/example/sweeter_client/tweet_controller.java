package com.example.sweeter_client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.json.JSONObject;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.sweeter_client.signin_Controller.toStringArray;
import static java.lang.System.in;

public class tweet_controller implements Initializable {

    @FXML
    private TextArea TweetTextArea;

    @FXML
    private Label TwetType;

    @FXML
    private Button back_button;

    @FXML
    private Button direct_button;

    @FXML
    private Label greenLabel;

    @FXML
    private Button prof_button;

    @FXML
    private Label redLabel;

    @FXML
    private Button search_button;

    @FXML
    private Button selectFileButton;

    @FXML
    private Label selectFileLabel;

    @FXML
    private Button timeline_button;

    @FXML
    private Button tweet_button;

    @FXML
    private Button tweetingButton;
    Tweet QTweet;
    public static String quoteTweetId = "";
    List <File> pictures;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setImageToButton(prof_button, "src/main/resources/com/example/sweeter_client/pictures/user.png");
        setImageToButton(direct_button, "src/main/resources/com/example/sweeter_client/pictures/direct.png");
        setImageToButton(search_button, "src/main/resources/com/example/sweeter_client/pictures/search.png");
        setImageToButton(timeline_button, "src/main/resources/com/example/sweeter_client/pictures/timeline.png");
        setImageToButton(tweet_button, "src/main/resources/com/example/sweeter_client/pictures/tweet.png");

        pictures = new ArrayList<>();
        TweetTextArea.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
        if (quoteTweetId.length() == 0) {
            TwetType.setText("Tweeting..");
        }

        String[] split = quoteTweetId.split("@");
        if (split[0].equals("T")) {
            TwetType.setText("ReTweeting..");
            Tweet reTweetTweet = null;
            try {
                reTweetTweet = getTweet(split[1]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            QTweet = reTweetTweet;
            TweetTextArea.setText(reTweetTweet.getText());
            TweetTextArea.setEditable(false);
            selectFileButton.setDisable(true);
            try {
                DownloadingTweetPictures(reTweetTweet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            for (int i = 0; i < reTweetTweet.getMediaPaths().size(); i++) {
                File f = new File("src/main/resources/com/example/sweeter_client/assets/" + reTweetTweet.getId() + "(" + i + ")" + ".png");
                pictures.add(f);
            }
        }
        else if (split[0].equals("R")) {
            TwetType.setText("Replying..");
        }
        else if (split[0].equals("Q")) {
            TwetType.setText("Quoting..");
        }

    }

    public void setImageToButton(Button b, String path) {
        Image image = new Image(Path.of(path).toUri().toString());
        ImageView view = new ImageView(image);
        view.setFitHeight(b.getPrefHeight() - 50);
        view.setFitWidth(b.getPrefWidth() - 50);
        b.setGraphic(view);
        selectFileLabel.setMaxWidth(400);
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

    public void SelectFile(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("pictures", "*.png"));
        List<File> f = fc.showOpenMultipleDialog(null);
        String res = "";
        if (f == null)
            return;
        for (int i = 0; i < f.size(); i++) {
            File file = f.get(i);
            res += file.getName();

            if (i != f.size() - 1)
                res += ", ";
        }
        pictures = f;
        selectFileLabel.setText(res);
    }
    public void Tweeting_Button_pressed(ActionEvent event) throws IOException {
        if (TwetType.getText().equals("Tweeting..") || TwetType.getText().equals("Replying..") || TwetType.getText().equals("Quoting..")) {
            Tweet tweet = new Tweet("", HelloApplication.loggedin_user.getId(), HelloApplication.loggedin_user.getId(), TweetTextArea.getText(), quoteTweetId, new Date(), 0, 0, 0);

            for (int i = 0; i < pictures.size(); i++)
                tweet.getMediaPaths().add("a");

            String tweetId = savingTweet(tweet);
            if (!tweetId.equals("Connection failed")) {
                savingPictures(tweetId);
                savingHashtags(tweetId, TweetTextArea.getText());
                greenLabel.setText("Tweet successfully Tweeted");
                redLabel.setText("");
            }
            else {
                redLabel.setText("Connection failed");
                greenLabel.setText("");
            }
            Clear();
        }
        else if (TwetType.getText().equals("ReTweeting..")) {
            Tweet tweet = new Tweet("", HelloApplication.loggedin_user.getId(), QTweet.getOwnerId(), TweetTextArea.getText(), quoteTweetId, new Date(), 0, 0, 0);

            for (int i = 0; i < pictures.size(); i++)
                tweet.getMediaPaths().add("a");

            String tweetId = savingTweet(tweet);
            if (!tweetId.equals("Connection failed")) {
                savingPictures(tweetId);
                savingHashtags(tweetId, TweetTextArea.getText());
                greenLabel.setText("Tweet successfully Tweeted");
                redLabel.setText("");
            }
            else {
                redLabel.setText("Connection failed");
                greenLabel.setText("");
            }
            Clear();
        }
    }

    public void savingHashtags(String tweetID, String text) throws IOException {
        String[] split = text.split("\\s+");
        for (String s: split) {
            if (s.length() >= 2 && s.charAt(0) == '#' && s.charAt(1) != '#') {
                String x = s.substring(1);
                System.out.println("checking = " + x);
                hashtagAdd(tweetID, x);
            }
        }
    }
    public void hashtagAdd(String tweetID, String hash) throws IOException {
        try {
            String response;
            URL url = new URL("http://localhost:8080/hashtag/" + hash + "/" + tweetID);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("JWT", HelloApplication.token);

            con.setRequestMethod("POST");
            con.setDoOutput(true);

            Reader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            for (int c; (c = in.read()) > 0; )
                sb.append((char) c);
            response = sb.toString();

            if (response.equals("Done!")) {
                System.out.println("hashtag saved successfully = " + hash);
            }
            else
                System.out.println(response);
        }
        catch (ConnectException e) {
            System.out.println("Connection failed");
        }
    }

    public String savingTweet(Tweet tweet) throws IOException {
        try {
            String response;
            URL url = new URL("http://localhost:8080/tweets/" +  tweet.getWriterId());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("JWT", HelloApplication.token);

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(tweet);

            byte[] postDataBytes = json.getBytes();

            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.getOutputStream().write(postDataBytes);

            Reader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            for (int c; (c = in.read()) > 0; )
                sb.append((char) c);
            response = sb.toString();
            return response;
        }
        catch (ConnectException e) {
            System.out.println("Connection failed");
            return "Connection failed";
        }
    }
    public static void DownloadingTweetPictures(Tweet tweet) throws IOException {
        for (int i = 0; i < tweet.getMediaPaths().size(); i++) {
            try {
                String response;
                URL url = new URL("http://localhost:8080/media/" + tweet.getOwnerId() + "/" + tweet.getId() + "(" + i + ")" + "/png");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                String t = con.getHeaderField("content-length");
                if (t == null || Integer.parseInt(t) == 7) {
                    continue;
                }
                Files.copy(con.getInputStream(), Path.of("src/main/resources/com/example/sweeter_client/assets/" + tweet.getId() + "(" + i + ")" + ".png"), StandardCopyOption.REPLACE_EXISTING);
            }
            catch (ConnectException e) {
                e.printStackTrace();
            }
        }
    }
    public static Tweet getTweet(String tweetid) throws IOException {
        try {
            HelloApplication m = new HelloApplication();
            URL url = new URL("http://localhost:8080/tweets/" + tweetid);
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
            JSONObject jsonObject = new JSONObject(response);
//            public Tweet(String id, String writerId, String ownerId, String text, String quoteTweetId, Date createdAt, int likes, int retweets, int replies)
            Tweet tweet = new Tweet(tweetid, jsonObject.getString("writerId"), jsonObject.getString("ownerId"), jsonObject.getString("text"), jsonObject.getString("quoteTweetId"), new Date(jsonObject.getLong("createdAt")), jsonObject.getInt("likes"), jsonObject.getInt("retweets"), jsonObject.getInt("replies"));
            String[] t = toStringArray(jsonObject.getJSONArray("mediaPaths"));
            for (String x: t)
                tweet.getMediaPaths().add(x);
            return tweet;
        }
        catch (ConnectException e) {
            return new Tweet("", "", "", "", "", new Date(), 0, 0, 0);
        }
    }
    public void savingPictures(String tweet_id) throws IOException {
        for (int i = 0; i < pictures.size(); i++) {
            File file = pictures.get(i);
            try {
                String response;
                URL url = new URL("http://localhost:8080/media/" + HelloApplication.loggedin_user.getId() + "/" + tweet_id + "(" + i + ")" + "/png");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestProperty("JWT", HelloApplication.token);

                System.out.println(HelloApplication.loggedin_user.getId() + "  " + HelloApplication.token);
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                OutputStream outputStream = con.getOutputStream();
                Files.copy(file.toPath(), outputStream);
                outputStream.close();

                Reader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                for (int c; (c = in.read()) > 0; )
                    sb.append((char) c);
                response = sb.toString();
                System.out.println(response);
            } catch (ConnectException e) {
                System.out.println("Connection failed");
            }
        }
    }
    public void Clear() {
        quoteTweetId = "";
        TwetType.setText("Tweeting..");
        pictures = new ArrayList<>();
        selectFileLabel.setText("");
        TweetTextArea.setText("");
        TweetTextArea.setEditable(true);
        selectFileButton.setDisable(false);
    }
}
