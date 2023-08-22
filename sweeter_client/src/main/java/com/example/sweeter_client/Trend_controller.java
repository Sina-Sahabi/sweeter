package com.example.sweeter_client;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Trend_controller implements Initializable {
    @FXML
    private Label Avatar_label;

    @FXML
    private Button back_button;

    @FXML
    private TextField hashtag1TextField;

    @FXML
    private TextField hashtag2TextField;

    @FXML
    private TextField hashtag3TextField;

    @FXML
    private Label header_label;
    @Override
    public void initialize(URL url, ResourceBundle rb) {

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
    public void Clear() {

    }
    public void generate_graph() throws IOException {
        ArrayList <String> vec = new ArrayList<>();
        if (hashtag1TextField.getText().length() != 0)
            vec.add(hashtag1TextField.getText());
        if (hashtag2TextField.getText().length() != 0)
            vec.add(hashtag2TextField.getText());
        if (hashtag3TextField.getText().length() != 0)
            vec.add(hashtag3TextField.getText());

        String tp = "";
        for (String s: vec)
            tp += "/" + s;

        URL url = new URL("http://localhost:8080/trend" + tp);
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
    }
}
