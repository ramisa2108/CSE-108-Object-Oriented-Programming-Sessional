import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.PrintWriter;

import java.net.Socket;
import java.util.ArrayList;


public class clientMain extends Application{

    public static receiverThread recieve;
    public static PrintWriter writeToServer;
    public static Socket connection;
    public static ArrayList<String>broadcastedMessages=new ArrayList<>();
    public static ArrayList<String>personalMessages=new ArrayList<>();
    public static ArrayList<String>showActive=new ArrayList<>();
    public static boolean loginStatus;

    public static void main(String[] args) throws Exception {

        connection=new Socket("localhost",5555);
        loginStatus=false;
        recieve=new receiverThread(connection);
        writeToServer=new PrintWriter(connection.getOutputStream(),true);

        Thread T1=new Thread(recieve);
        T1.start();
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root= FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        Scene scene=new Scene(root,800,500);
        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest(e->{
           try {
               writeToServer.println("exit");
               recieve.close();
               connection.close();
           }catch (Exception ee){
               ee.printStackTrace();
           }

       });
        primaryStage.show();

    }
}