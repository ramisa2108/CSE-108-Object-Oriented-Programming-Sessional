import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class mainPageController implements Initializable {

    @FXML
    private ListView<String> onlineList;

    @FXML
    private ListView<String> messageList;

    @FXML
    private BorderPane root;

    @FXML
    private ComboBox<String> choice;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        choice.getItems().setAll("Broadcasts", "Personal");
        choice.setStyle("-fx-background-color: #80ccb1;-fx-font: 20px \"Comic Sans MS\";");
        choice.setPromptText("Messages");


    }

    @FXML
    void logoutPressed(ActionEvent event) throws Exception {

        SenderBox.display("S#logout#");
        Thread.sleep(200);

        if (!clientMain.loginStatus) {
            try {
                clientMain.writeToServer.println("exit");
                clientMain.recieve.close();
                clientMain.connection.close();
                System.exit(0);
            }catch (Exception ee){
                ee.printStackTrace();
            }
        }

    }

    @FXML
    void CMessagePressed(ActionEvent event) {

        SenderBox.display("C#");
    }

    @FXML
    void BMessagePressed(ActionEvent event) {
        SenderBox.display("B#");

    }

    @FXML
    void showPressed(ActionEvent event) throws Exception {

        SenderBox.display("S#show#");
        Thread.sleep(200);
        onlineList.getItems().setAll(clientMain.showActive);
        onlineList.setStyle("-fx-font: 20px \"Comic Sans MS\"; -fx-control-inner-background: #3c4743 ;");

    }

    @FXML
    void chooseOption() {

       if(choice.getValue()!=null) {

           ArrayList<String>showArray=new ArrayList<>();

           if (choice.getSelectionModel().isSelected(0)) {
               showArray=clientMain.broadcastedMessages;}
           else if(choice.getSelectionModel().isSelected(1)) {
               showArray = clientMain.personalMessages;
           }

               messageList.getItems().clear();
               int size = showArray.size();

               for (int i = size - 1; i >= 0; i--) {
                   messageList.getItems().add(showArray.get(i));
               }

               messageList.setStyle("-fx-font: 20px \"Comic Sans MS\"; -fx-control-inner-background: #3c4743 ;");

           }

       }
    }



