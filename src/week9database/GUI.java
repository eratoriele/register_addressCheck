package week9database;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import java.net.URLConnection;
import java.sql.*;

class GUI {
    private JButton registerButton;
    private JTextField cityField;
    private JTextField zipField;
    private JTextField street_numberField;
    private JTextField street_nameField;
    private JTextField usernameTextField;
    private JTextField passwordField;
    private JPanel panelMain;

    private GUI() {
        registerButton.addActionListener(e -> {
            String username = usernameTextField.getText();
            String password = passwordField.getText();
            String street_name = street_nameField.getText();
            String street_number = street_numberField.getText();
            String zip = zipField.getText();
            String city = cityField.getText();


            String url = "jdbc:sqlserver://DESKTOP-ICNMN9R\\SQLEXPRESS:1433;databaseName=uni;user=era;password=123909321a";

            try (Connection con = DriverManager.getConnection(url); Statement stmt = con.createStatement()) {

                String ur = street_name + "+" + street_number + "+" + zip + "+" + city;             //Get the URL
                ur = ur.replace(' ', '+');
                ur = "https://dawa.aws.dk/autocomplete?caretpos=28&fuzzy=&q=" + ur + "&startfra=adresse&type=adresse";
                URL url1 = new URL(ur);

                URLConnection req = url1.openConnection();
                req.connect();

                JsonParser jp = new JsonParser();
                JsonElement root = jp.parse(new InputStreamReader((InputStream) req.getContent()));     //Turn the url into
                JsonArray rootarr = root.getAsJsonArray();                                              //JsonObject with gson library
                JsonObject jo = rootarr.get(0).getAsJsonObject().getAsJsonObject("data");

                if (jo.get("vejnavn").getAsString().equals(street_name) && jo.get("husnr").getAsString().equals(street_number))     //Check if
                    if(jo.get("postnr").getAsString().equals(zip) && jo.get("postnrnavn").getAsString().equals(city)){              //Given adress
                        String SQL = "insert into register values('";                                                               //exists
                        SQL = SQL + username+"', '"+password+"', '"+street_name+"', "+street_number+", "+zip+", '"+city +"');";

                        int a = stmt.executeUpdate(SQL);          //Add the registered account to the DB using JDBC library
                    }

            }
            // Exit if there are any errors ex. = username already exists
            catch (Exception el) {
                el.printStackTrace();
                System.exit(-1);
            }


        });

    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Register");
        frame.setContentPane(new GUI().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
