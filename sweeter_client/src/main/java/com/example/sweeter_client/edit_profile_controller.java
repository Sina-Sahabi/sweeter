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
import java.util.List;
import java.util.ResourceBundle;

public class edit_profile_controller implements Initializable {
    @FXML
    private Button Avatar_button;

    @FXML
    private Button back_button;

    @FXML
    private TextArea bio_tf;

    @FXML
    private Button header_button;

    @FXML
    private TextField locaton_tf;

    @FXML
    private Button save_button;
    @FXML
    private Button refresh_button;

    @FXML
    private TextField website_tf;
    @FXML
    private Label Avatar_label;
    @FXML
    private Label header_label;
    private File AvatarFile = null;
    private File HeaderFile = null;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Bio bio = null;
        try {
            bio = gettingBio(HelloApplication.loggedin_user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (bio == null) {
            System.out.println("Connection failed");
        }
        else {
            bio_tf.setText(bio.getBiography());
            website_tf.setText(bio.getWebsite());
            locaton_tf.setText(bio.getLocation());
        }
        AvatarFile = null;
        HeaderFile = null;
        header_label.setText("");
        Avatar_label.setText("");
        header_label.setMaxWidth(500);
        Avatar_label.setMaxWidth(500);
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
    public void refresh_button_Entered() {
        refresh_button.setStyle("-fx-background-color: #9136FF;");
    }
    public void refresh_button_exit() {
        refresh_button.setStyle("-fx-background-color: #192841;");
    }
    public void Clear() {

    }
    public void refreshing() throws IOException {
        Bio bio = null;
        try {
            bio = gettingBio(HelloApplication.loggedin_user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (bio == null) {
            System.out.println("Connection failed");
        }
        else {
            bio_tf.setText(bio.getBiography());
            website_tf.setText(bio.getWebsite());
            locaton_tf.setText(bio.getLocation());
        }
        AvatarFile = null;
        HeaderFile = null;
        header_label.setText("");
        Avatar_label.setText("");
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
            return bio;
        }
        catch (ConnectException e) {
            return null;
        }
    }

    public void AvatarFileChooser(ActionEvent event) throws Exception {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pictures", "*.png"));
        File f = fc.showOpenDialog(null);

        if (f != null) {
            AvatarFile = f;
            Avatar_label.setText("Selected File::" + f.getAbsolutePath());
        }
    }
    public void HeaderFileChooser(ActionEvent event) throws Exception {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pictures", "*.png"));
        File f = fc.showOpenDialog(null);

        if (f != null) {
            HeaderFile = f;
            header_label.setText("Selected File::" + f.getAbsolutePath());
        }
    }

    public void SaveChangePressed(ActionEvent event) throws Exception {
        // updating BIO
        try {
            Bio bio = new Bio(HelloApplication.loggedin_user.getId(), bio_tf.getText(), locaton_tf.getText(), website_tf.getText());

            String response;
            URL url = new URL("http://localhost:8080/bios");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();


            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(bio);

            byte[] postDataBytes = json.getBytes();

            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.getOutputStream().write(postDataBytes);

            Reader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            for (int c; (c = in.read()) > 0; )
                sb.append((char) c);
            response = sb.toString();

            if (response.equals("this is done!"))
                System.out.println(response);
            else
                System.out.println("server error");
        }
        catch (ConnectException e) {
            System.out.println("Connection failed");
        }

        if (AvatarFile != null) {
            try {
                String response;
                URL url = new URL("http://localhost:8080/media/" + HelloApplication.loggedin_user.getId() + "/Avatar/png");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestProperty("JWT", HelloApplication.token);

                con.setRequestMethod("POST");
                con.setDoOutput(true);
                OutputStream outputStream = con.getOutputStream();
                Files.copy(AvatarFile.toPath(), outputStream);
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
        if (HeaderFile != null) {
            try {
                String response;
                URL url = new URL("http://localhost:8080/media/" + HelloApplication.loggedin_user.getId() + "/Header/png");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty("JWT", HelloApplication.token);
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                OutputStream outputStream = con.getOutputStream();
                Files.copy(HeaderFile.toPath(), outputStream);
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
}
