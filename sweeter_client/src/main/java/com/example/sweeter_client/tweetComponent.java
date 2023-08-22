package com.example.sweeter_client;

import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.sweeter_client.TweetView_controller.getAllTweets;
import static com.example.sweeter_client.profileComponent.getAvatar;
import static com.example.sweeter_client.profileComponent.getUser;
import static com.example.sweeter_client.signin_Controller.toStringArray;
import static com.example.sweeter_client.tweet_controller.getTweet;
import com.example.sweeter_client.Like;

public class tweetComponent extends AnchorPane {
    Label user_idLabel;
    Circle circleClipProfile;
    ImageView AvatarImageView;
    Label nameLabel;
    JFXTextArea TweetBody;
    Tweet tweet;
    Image AvatarImage;
    User user = new User("", "", "", "", "", "", "", new Date());
    ScrollPane scrollerImage;
    ScrollPane scrollPaneQuote;
    HBox ImageHBox;
    Label QuoteText;
    SimpletweetComponent QuoteTweet;
    VBox QuoteTweetVBox;
    Rectangle Qrect;
    Label ReTweet;
    Button replyButton;
    Button reTweetButton;
    Button LikeButton;
    Label NumReplyLabel;
    Label NumRetweetLabel;
    Label NumLikeLabel;
    Label dateLabel;
    Label QuotThisLabel;
    Label SeeRepLabel;

    Rectangle rectangleClip;
    int x = 0;
    int y = 20;
    public tweetComponent(Tweet tweet) throws IOException {
        this.tweet = tweet;
        this.user = getUser(tweet.getOwnerId());


        AvatarImage = getAvatar(user);
        AvatarImageView = new ImageView(AvatarImage);
        user_idLabel = new Label("@" + user.getId());
        nameLabel = new Label(user.getFirstName());
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        dateLabel = new Label(f.format(tweet.getCreatedAt()));

        QuotThisLabel = new Label("Quote this tweet");
        SeeRepLabel = new Label("See Replies");

        rectangleClip = new Rectangle(20, 20);
        ImageView favStar = new ImageView(new Image(Path.of("src/main/resources/com/example/sweeter_client/pictures/Star.png").toUri().toString()));
        favStar.setFitWidth(20);
        favStar.setFitHeight(20);
        favStar.setClip(rectangleClip);

        NumReplyLabel = new Label(Integer.toString(NumberOfReply(tweet.getId())));
        NumRetweetLabel = new Label(Integer.toString(NumberOfRetweet(tweet.getId())));
        NumLikeLabel = new Label(Integer.toString(NumberOfLikes(tweet.getId())));

        circleClipProfile = new Circle(30);
        TweetBody = new JFXTextArea();
        TweetBody.setText(tweet.getText());
        scrollerImage = new ScrollPane();
        ImageHBox = new HBox();
        ImageHBox.setSpacing(50);
        ImageHBox.setStyle("-fx-background-color: black;");
        scrollerImage.setFocusTraversable(false);
//        "-fx-focus-color: black;"
        scrollerImage.setStyle("-fx-border-color: #192841;" + "-fx-background: #192841;" + "track-background-color: #192841;" + "-fx-focus-color: transparent;" + "-fx-faint-focus-color: transparent;");
        scrollerImage.setContent(ImageHBox);
        QuoteText = new Label();
        ReTweet = new Label();
        scrollPaneQuote = new ScrollPane();
        Qrect = new Rectangle(560, 300);
        Qrect.setStyle("-fx-fill: transparent; -fx-stroke: white; -fx-stroke-width: 5;");
        if (tweet.getQuoteTweetId().split("@").length == 2 && tweet.getQuoteTweetId().split("@")[0].equals("Q")) {
            Tweet QTweet = getTweet(tweet.getQuoteTweetId().split("@")[1]);
            User Quser = getUser(QTweet.getOwnerId());
            QuoteText.setText("Quoted from " + Quser.getFirstName() + ":");
            QuoteTweet = new SimpletweetComponent(QTweet);
        }
        if (tweet.getQuoteTweetId().split("@").length == 2 && tweet.getQuoteTweetId().split("@")[0].equals("T")) {
            x = 50;
            User one = getUser(tweet.getWriterId());
            User two = getUser(tweet.getOwnerId());
            ReTweet.setText(one.getFirstName() + " retweeting from " + two.getFirstName());
        }
        QuoteTweetVBox = new VBox();
        scrollPaneQuote.setContent(QuoteTweet);
        scrollPaneQuote.setStyle("-fx-border-color: #192841;" + "-fx-background: #192841;" + "track-background-color: #192841;" + "-fx-focus-color: transparent;" + "-fx-faint-focus-color: transparent;");
        QuoteTweetVBox.setStyle("-fx-background-color: black;");

        replyButton = new Button();
        reTweetButton = new Button();
        LikeButton = new Button();

        setImageToButton(replyButton, "src/main/resources/com/example/sweeter_client/pictures/reply.png");
        setImageToButton(reTweetButton, "src/main/resources/com/example/sweeter_client/pictures/retweet.png");
        if (!IsLike(HelloApplication.loggedin_user.getId(), tweet.getId()))
            setImageToButton(LikeButton, "src/main/resources/com/example/sweeter_client/pictures/like1.png");
        else
            setImageToButton(LikeButton, "src/main/resources/com/example/sweeter_client/pictures/like2.png");

        DownloadingTweetPictures(tweet);

        setAction();
        setConfig();
        setLocation();

//        this.getChildren().addAll(user_idLabel, circleClipProfile, nameLabel, TweetBody);
        this.getChildren().addAll(SeeRepLabel, QuotThisLabel, dateLabel, NumLikeLabel, NumRetweetLabel, NumReplyLabel, LikeButton, circleClipProfile, nameLabel, user_idLabel, TweetBody, scrollerImage, QuoteText, QuoteTweetVBox, ReTweet, replyButton, reTweetButton);
        if (NumberOfLikes(tweet.getId()) >= 10) {
            this.getChildren().add(favStar);
        }
        if (tweet.getQuoteTweetId().split("@").length == 2 && tweet.getQuoteTweetId().split("@")[0].equals("Q")) {
            QuoteTweetVBox.getChildren().add(QuoteTweet);
            this.getChildren().addAll(scrollPaneQuote, Qrect);
        }
        else {
            this.setPrefSize(580, 680);
        }
    }
    private void setConfig() throws IOException {
        this.setPrefSize(580, 1000);
        this.setStyle("-fx-background-color: #192841");

        user_idLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
        user_idLabel.setTextFill(Paint.valueOf("gray"));

        dateLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
        dateLabel.setTextFill(Paint.valueOf("gray"));

        circleClipProfile.setFill(new ImagePattern(AvatarImage));

        nameLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        nameLabel.setTextFill(Paint.valueOf("white"));

        NumLikeLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        NumLikeLabel.setTextFill(Paint.valueOf("white"));

        NumReplyLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        NumReplyLabel.setTextFill(Paint.valueOf("white"));

        NumRetweetLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        NumRetweetLabel.setTextFill(Paint.valueOf("white"));

        QuotThisLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
        QuotThisLabel.setTextFill(Paint.valueOf("white"));

        SeeRepLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
        SeeRepLabel.setTextFill(Paint.valueOf("white"));

        TweetBody.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
        TweetBody.setPrefSize(400, 150);
        TweetBody.setStyle("-fx-text-fill: white;" + "-jfx-focus-color: #192841;" + "-jfx-unfocus-color: #192841;") ;
        TweetBody.setEditable(false);
        TweetBody.setWrapText(true);

        scrollerImage.setPrefSize(400, 150);

        QuoteText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        QuoteText.setTextFill(Paint.valueOf("white"));

        ReTweet.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        ReTweet.setTextFill(Paint.valueOf("white"));

        replyButton.setPrefSize(20, 20);
        replyButton.setStyle("-fx-background-color: #192841");

        reTweetButton.setPrefSize(20, 20);
        reTweetButton.setStyle("-fx-background-color: #192841");

        LikeButton.setPrefSize(20, 20);
        LikeButton.setStyle("-fx-background-color: #192841");

    }
    private void setLocation() {
        AnchorPane.setTopAnchor(circleClipProfile, 20.0 + x);
        AnchorPane.setLeftAnchor(circleClipProfile, 20.0);

        AnchorPane.setTopAnchor(nameLabel, 20.0 + x);
        AnchorPane.setLeftAnchor(nameLabel, 90.0);

        AnchorPane.setTopAnchor(user_idLabel, 45.0 + x);
        AnchorPane.setLeftAnchor(user_idLabel, 90.0);

        AnchorPane.setTopAnchor(TweetBody, 90.0 + x);
        AnchorPane.setLeftAnchor(TweetBody, 90.0);

        AnchorPane.setTopAnchor(scrollerImage, 270.0 + x);
        AnchorPane.setLeftAnchor(scrollerImage, 90.0);

        AnchorPane.setTopAnchor(QuoteText, 450.0 + x);
        AnchorPane.setLeftAnchor(QuoteText, 90.0);

        AnchorPane.setTopAnchor(scrollPaneQuote, 520.0 + x);
        AnchorPane.setLeftAnchor(scrollPaneQuote, 20.0);

        AnchorPane.setTopAnchor(Qrect, 490.0 + x);
        AnchorPane.setLeftAnchor(Qrect, 10.0);

        AnchorPane.setTopAnchor(ReTweet, 10.0);
        AnchorPane.setLeftAnchor(ReTweet, 20.0);

        AnchorPane.setTopAnchor(QuotThisLabel, 20.0 + x);
        AnchorPane.setRightAnchor(QuotThisLabel, 20.0);

        AnchorPane.setTopAnchor(SeeRepLabel, 50.0 + x);
        AnchorPane.setRightAnchor(SeeRepLabel, 20.0);

        if (tweet.getQuoteTweetId().split("@").length == 2 && tweet.getQuoteTweetId().split("@")[0].equals("Q")) {
            int t = 380;
            AnchorPane.setTopAnchor(replyButton, 435.0 + x + t);
            AnchorPane.setLeftAnchor(replyButton, 100.0 + y);

            AnchorPane.setTopAnchor(reTweetButton, 430.0 + x + t);
            AnchorPane.setLeftAnchor(reTweetButton, 230.0 + y);

            AnchorPane.setTopAnchor(replyButton, 435.0 + x + t);
            AnchorPane.setLeftAnchor(replyButton, 100.0 + y);

            AnchorPane.setTopAnchor(LikeButton, 430.0 + x + t);
            AnchorPane.setLeftAnchor(LikeButton, 360.0 + y);

            AnchorPane.setTopAnchor(NumReplyLabel, 510.0 + x + t);
            AnchorPane.setLeftAnchor(NumReplyLabel, 130.0 + y);

            AnchorPane.setTopAnchor(NumRetweetLabel, 510.0 + x + t);
            AnchorPane.setLeftAnchor(NumRetweetLabel, 265.0 + y);

            AnchorPane.setTopAnchor(NumLikeLabel, 510.0 + x + t);
            AnchorPane.setLeftAnchor(NumLikeLabel, 390.0 + y);

            AnchorPane.setTopAnchor(dateLabel, 570.0 + x + t);
            AnchorPane.setLeftAnchor(dateLabel, 90.0);
        }
        else {
            AnchorPane.setTopAnchor(replyButton, 435.0 + x);
            AnchorPane.setLeftAnchor(replyButton, 100.0 + y);

            AnchorPane.setTopAnchor(reTweetButton, 430.0 + x);
            AnchorPane.setLeftAnchor(reTweetButton, 230.0 + y);

            AnchorPane.setTopAnchor(replyButton, 435.0 + x);
            AnchorPane.setLeftAnchor(replyButton, 100.0 + y);

            AnchorPane.setTopAnchor(LikeButton, 430.0 + x);
            AnchorPane.setLeftAnchor(LikeButton, 360.0 + y);

            AnchorPane.setTopAnchor(NumReplyLabel, 510.0 + x);
            AnchorPane.setLeftAnchor(NumReplyLabel, 130.0 + y);

            AnchorPane.setTopAnchor(NumRetweetLabel, 510.0 + x);
            AnchorPane.setLeftAnchor(NumRetweetLabel, 265.0 + y);

            AnchorPane.setTopAnchor(NumLikeLabel, 510.0 + x);
            AnchorPane.setLeftAnchor(NumLikeLabel, 390.0 + y);

            AnchorPane.setTopAnchor(dateLabel, 570.0 + x);
            AnchorPane.setLeftAnchor(dateLabel, 90.0);
        }


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
        Qrect.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Tweet T = new Tweet("", "", "", "", "", new Date(), 0, 0, 0);
                try {
                    T = getTweet(tweet.getQuoteTweetId().split("@")[1]);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                TweetView_controller.tweet = T;
                HelloApplication m = new HelloApplication();
                try {
                    m.changeScene(10);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        ReTweet.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                UserView_controller.users = new ArrayList<>();
                User Wuser = null;
                try {
                    Wuser = getUser(tweet.getWriterId());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                UserView_controller.users.add(Wuser);
                HelloApplication m = new HelloApplication();
                try {
                    m.changeScene(9);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        replyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                HelloApplication m = new HelloApplication();
                tweet_controller.quoteTweetId = "R@" + tweet.getId();
                try {
                    m.changeScene(5);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        reTweetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                HelloApplication m = new HelloApplication();
                tweet_controller.quoteTweetId = "T@" + tweet.getId();
                try {
                    m.changeScene(5);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        SeeRepLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                HelloApplication m = new HelloApplication();
                TweetView_controller.tweet = tweet;
                try {
                    m.changeScene(10);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        QuotThisLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                HelloApplication m = new HelloApplication();
                tweet_controller.quoteTweetId = "Q@" + tweet.getId();
                try {
                    m.changeScene(5);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        LikeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if (!IsLike(HelloApplication.loggedin_user.getId(), tweet.getId())) {
                        URL url = new URL("http://localhost:8080/like/" + HelloApplication.loggedin_user.getId() + "/" + tweet.getId());
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestProperty("JWT", HelloApplication.token);
                        con.setRequestMethod("POST");
                        int responseCode = con.getResponseCode();
                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String inputline;
                        StringBuffer response1 = new StringBuffer();
                        while ((inputline = in.readLine()) != null) {
                            response1.append(inputline);
                        }
                        in.close();
                        String response = response1.toString();

                        if (response.equals("Done!")) {
                            setImageToButton(LikeButton, "src/main/resources/com/example/sweeter_client/pictures/like2.png");
                            NumLikeLabel.setText(Integer.toString(NumberOfLikes(tweet.getId())));
                        }
                        else {
                            System.out.println(response);
                        }
                    }
                    else {
                        URL url = new URL("http://localhost:8080/like/" + HelloApplication.loggedin_user.getId() + "/" + tweet.getId());
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestProperty("JWT", HelloApplication.token);
                        con.setRequestMethod("DELETE");
                        int responseCode = con.getResponseCode();
                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String inputline;
                        StringBuffer response1 = new StringBuffer();
                        while ((inputline = in.readLine()) != null) {
                            response1.append(inputline);
                        }
                        in.close();
                        String response = response1.toString();

                        if (response.equals("Done!")) {
                            setImageToButton(LikeButton, "src/main/resources/com/example/sweeter_client/pictures/like1.png");
                            NumLikeLabel.setText(Integer.toString(NumberOfLikes(tweet.getId())));
                        }
                        else {
                            System.out.println(response);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void DownloadingTweetPictures(Tweet tweet) throws IOException {
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
                Image img = new Image(Path.of("src/main/resources/com/example/sweeter_client/assets/" + tweet.getId() + "(" + i + ")" + ".png").toUri().toString());
                ImageView imgView = new ImageView(img);
                imgView.setFitHeight(130);
                imgView.setFitWidth(390);
                ImageHBox.getChildren().add(imgView);
            }
            catch (ConnectException e) {
                e.printStackTrace();
            }
        }
    }
    public void setImageToButton(Button b, String path) {
        Image image = new Image(Path.of(path).toUri().toString());
        ImageView view = new ImageView(image);
        b.setGraphic(view);
    }

    public boolean IsLike(String userId, String tweetId) throws IOException {
        ArrayList <Like> likes = getAllLikes();
        for (Like l: likes) {
            if (l.getLiker().equals(userId) && l.getLiked().equals(tweetId))
                return true;
        }
        return false;
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
        ArrayList <Like> likes = new ArrayList<>();
        for (String t: users) {
            JSONObject obj = new JSONObject(t);
            Like l = new Like(obj.getString("liker"), obj.getString("liked"));
            likes.add(l);
        }
        return likes;
    }

    public int NumberOfReply(String tweetId) throws IOException {
        ArrayList <Tweet> tweets = getAllTweets();
        int res = 0;
        for (Tweet t: tweets) {
            if (t.getQuoteTweetId().split("@").length == 2 && t.getQuoteTweetId().split("@")[0].equals("R") && t.getQuoteTweetId().split("@")[1].equals(tweetId))
                res++;
        }
        return res;
    }
    public int NumberOfRetweet(String tweetId) throws IOException {
        ArrayList <Tweet> tweets = getAllTweets();
        int res = 0;
        for (Tweet t: tweets) {
            if (t.getQuoteTweetId().split("@").length == 2 && t.getQuoteTweetId().split("@")[0].equals("T")&& t.getQuoteTweetId().split("@")[1].equals(tweetId))
                res++;
        }
        return res;
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

}
