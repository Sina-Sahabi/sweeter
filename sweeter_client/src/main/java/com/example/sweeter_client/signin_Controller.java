package com.example.sweeter_client;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;

public class signin_Controller implements Initializable {
    @FXML
    private ComboBox Countries;

    @FXML
    private TextField Email_tf;

    @FXML
    private TextField FirstName_tf;

    @FXML
    private Label GreenLabel;

    @FXML
    private TextField LastName_tf;

    @FXML
    private PasswordField PasswordCheck_tf;

    @FXML
    private PasswordField Password_tf;

    @FXML
    private TextField Phone_tf;

    @FXML
    private Button Register_button;

    @FXML
    private TextField Username_tf;

    @FXML
    private Button back_button;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label wrong_label;
    String Country = "";
    String date = "";
    public void goBack() throws Exception {
        Clear();
        HelloApplication m = new HelloApplication();
        m.changeScene(1);
    }
    public void Select(ActionEvent event) {
        Country = Countries.getSelectionModel().getSelectedItem().toString();
    }
    public void getDate(ActionEvent event) {
        date = datePicker.getValue().toString();
    }
    public void Back_button_Entered() {
        back_button.setStyle("-fx-background-color: #9136FF;");
    }
    public void Back_button_exit() {
        back_button.setStyle("-fx-background-color: #00093C;");
    }
    public void register_button_entered() {
        Register_button.setStyle("-fx-background-color: #2200A9;");
    }
    public void register_button_exit() {
        Register_button.setStyle("-fx-background-color: #00093C;");
    }

    public void Register_pressed(ActionEvent event) throws Exception {
        boolean a1 = FirstName_tf.getText().length() == 0;
        boolean a2 = LastName_tf.getText().length() == 0;
        boolean a3 = Username_tf.getText().length() == 0;
        boolean a4 = Password_tf.getText().length() == 0;
        boolean a5 = Email_tf.getText().length() == 0;
        boolean a6 = Phone_tf.getText().length() == 0;
        boolean a7 = Country.length() == 0;
        boolean a8 = date.length() == 0;
        boolean a9 = PasswordCheck_tf.getText().length() == 0;
        if (a1 || a2 || a3 || a4 || (a5 && a6) || a7 || a8 || a9) {
            wrong_label.setText("please enter all fields");
        }
        else if (Email_tf.getText().length() != 0 && !isValidEmailAddress(Email_tf.getText())) {
            wrong_label.setText("Email is invalid");
        }
        else if (!isValidPass(Password_tf.getText())) {
            wrong_label.setText("Password is invalid");
        }
        else if (!PasswordCheck_tf.getText().equals(Password_tf.getText())) {
            wrong_label.setText("Passwords are not equal");
        }
        else {
            try {
                String response;
                {
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
                    response = response1.toString();
                }
                JSONArray jsonObject = new JSONArray(response);
                String[] users = toStringArray(jsonObject);
                boolean Email_existed = false;
                for (String t: users) {
                    JSONObject obj = new JSONObject(t);
                    User user = new User(obj.getString("id"), obj.getString("firstName"), obj.getString("lastName"), obj.getString("email"), obj.getString("phoneNumber"), obj.getString("password"), obj.getString("country"), null);
                    if (user.getEmail().equals(Email_tf.getText()) && Email_tf.getText().length() != 0)
                        Email_existed = true;
                    if (user.getPhoneNumber().equals(Phone_tf.getText()) && Phone_tf.getText().length() != 0)
                        Email_existed = true;
                }
                if (Email_existed) {
                    wrong_label.setText("Email or Phone existed");
                }
                else {
                    {
                        URL url = new URL("http://localhost:8080/users/" + Username_tf.getText());
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
                    }
                    if (!response.equals("No User")) {
                        wrong_label.setText("Username exist");
                    } else {
                        DateFormat format = new SimpleDateFormat("yyyy-mm-dd");
                        Date date1 = format.parse(date);
                        User user = new User(Username_tf.getText(), FirstName_tf.getText(), LastName_tf.getText(), Email_tf.getText(), Phone_tf.getText(), Password_tf.getText(), Country, date1);
                        //sending post request
                        URL url = new URL("http://localhost:8080/users");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();


                        ObjectMapper objectMapper = new ObjectMapper();
                        String json = objectMapper.writeValueAsString(user);

                        byte[] postDataBytes = json.getBytes();

                        con.setRequestMethod("POST");
                        con.setDoOutput(true);
                        con.getOutputStream().write(postDataBytes);

                        Reader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        for (int c; (c = in.read()) > 0; )
                            sb.append((char) c);
                        response = sb.toString();

                        if (response.equals("this is done!")) {
                            Clear();
                            GreenLabel.setText("Register completed");
                        } else
                            wrong_label.setText("Server error");
                    }
                }
            }
            catch (ConnectException e) {
                wrong_label.setText("connection failed");
            }
        }
    }
    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
    public static boolean isValidPass(String pass) {
        boolean cap = false, small = false;
        for (char c: pass.toCharArray()) {
            if (Character.isLowerCase(c))
                small = true;
            if (Character.isUpperCase(c))
                cap = true;
        }
        return cap && small && pass.length() >= 6;
    }
    public void Clear() {
        FirstName_tf.setText("");
        LastName_tf.setText("");
        Username_tf.setText("");
        Phone_tf.setText("");
        Password_tf.setText("");
        Email_tf.setText("");
        GreenLabel.setText("");
        wrong_label.setText("");
        PasswordCheck_tf.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList <String> list = FXCollections.observableArrayList("IRAN", "USA", "IRAQ", "TURKEY", "SPAIN", "UK", "DENMARK", "RUSSIA");
        Countries.setItems(list);
    }
    public static String[] toStringArray(JSONArray array) {
        if(array == null)
            return new String[0];

        String[] arr = new String[array.length()];
        for(int i = 0; i < arr.length; i++)
            arr[i] = array.optString(i);
        return arr;
    }
}
