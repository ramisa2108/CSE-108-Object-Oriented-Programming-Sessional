import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginPageController implements Initializable{

    @FXML
    private TextField NameText,typeText;
    @FXML
    private PasswordField PasswordText;
    @FXML
    private BorderPane root;
    @FXML
    private Label messageText;
    @FXML
    private Button tryAgainButton;

    private String Name,Password,type;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Name="";
        Password="";
        type="";
        messageText.setText("");

    }

    public void loginPressed() throws  Exception{

        Name=NameText.getText();
        Password=PasswordText.getText();
        type=typeText.getText();

        clientMain.writeToServer.println("L#"+Name+"#"+Password+"#"+type);
        Thread.sleep(200);

        if(clientMain.loginStatus){

             BorderPane pane=FXMLLoader.load(getClass().getResource("mainPage.fxml"));
             root.getChildren().setAll(pane);
        }

        else{

            messageText.setVisible(true);
            messageText.setText(clientMain.recieve.getMessage());
            tryAgainButton.setVisible(true);

        }

    }
    public void tryAgainPressed(){

        messageText.setVisible(false);
        tryAgainButton.setVisible(false);
    }

}
