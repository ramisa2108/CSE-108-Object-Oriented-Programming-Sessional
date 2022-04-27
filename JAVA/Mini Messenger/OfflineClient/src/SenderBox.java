import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

public class SenderBox {


    public static void display(String type){

        VBox vBox=new VBox(50);
        vBox.setStyle("-fx-background-color: #3c4743");
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20,20,20,20));

        HBox hBox1=new HBox(20);
        HBox hBox2=new HBox(20);
        HBox hBox3=new HBox(20);

        Label rec=new Label("Recipient");
        rec.setStyle("-fx-background-color: #80ccb1;-fx-font: 20px \"Comic Sans MS\";");

        Label message=new Label("Message");
        message.setStyle("-fx-background-color: #80ccb1;-fx-font: 20px \"Comic Sans MS\";");

        Label file=new Label("Filepath");
        file.setStyle("-fx-background-color: #80ccb1;-fx-font: 20px \"Comic Sans MS\";");

        Label messageLabel=new Label();
        messageLabel.setStyle("-fx-background-color: #80ccb1;-fx-font: 20px \"Comic Sans MS\";");

        TextField messageText=new TextField();
        messageText.setStyle("-fx-font: 20px \"Comic Sans MS\";");

        TextField fileText=new TextField();
        fileText.setStyle("-fx-font: 20px \"Comic Sans MS\";");

        TextField recText=new TextField();
        recText.setStyle("-fx-font: 20px \"Comic Sans MS\";");

        Button send=new Button("Send");
        send.setStyle("-fx-background-color: #80ccb1;-fx-font: 20px \"Comic Sans MS\";");

        hBox1.getChildren().addAll(rec,recText);
        hBox2.getChildren().addAll(message,messageText);
        hBox3.getChildren().addAll(file,fileText);

         if(type.startsWith("S")){
            vBox.getChildren().addAll(hBox2);
        }else if(type.startsWith("B")){
            vBox.getChildren().addAll(hBox2,messageLabel);
        }else if(type.startsWith("C")){
            vBox.getChildren().addAll(hBox1,hBox2,hBox3,messageLabel);
        }

        vBox.getChildren().add(send);
        Scene scene=new Scene(vBox,500,400);

        Stage window=new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        send.setOnAction(e->{

            if(type.startsWith("S")){

                String m=messageText.getText();
                if(m.equalsIgnoreCase(""))
                    m=" ";
                clientMain.writeToServer.println(type+m);
                window.close();


            }else if(type.startsWith("B")){

                messageLabel.setText("");
                String m=messageText.getText();
                if(m==null) m=" ";
                clientMain.writeToServer.println(type+m);

                try{
                    Thread.sleep(100);
                }catch (Exception ee){
                    ee.printStackTrace();
                }

                messageLabel.setText(clientMain.recieve.getMessage());

            }
            else if(type.startsWith("C")){

                messageLabel.setText("");
               if(recText.getText()==null){

                   messageLabel.setText("Enter Recipient:");
               }else{

                   String m=messageText.getText();
                   String r=recText.getText();
                   String f=fileText.getText();

                   if(f.equalsIgnoreCase("")) f="null";
                   else{
                       try{
                           File fileF=new File(f);
                           FileInputStream fis = new FileInputStream(fileF);
                           BufferedInputStream bis = new BufferedInputStream(fis);

                       }catch (Exception er){
                           System.out.println("No such file");
                           messageLabel.setText("Invalid File. ");
                           f="null";

                       }
                   }
                   clientMain.writeToServer.println(type+r+"#"+m+"#"+f);
                   if(!f.equalsIgnoreCase("null")){
                       if(sendFile(f))
                           messageLabel.setText("");
                       else messageLabel.setText("Invalid File. ");
                   }


                   try{
                       Thread.sleep(200);
                   }catch (Exception w){
                       w.printStackTrace();
                   }
                   String res=clientMain.recieve.getMessage();
                   if(res.startsWith("Message")) {
                       messageLabel.setText(messageLabel.getText()+res);
                   }
               }

            }
        });
        window.setScene(scene);
        window.setTitle("Send");
        window.showAndWait();

    }
    synchronized public static boolean sendFile(String filePath){

        try {

            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            OutputStream outputStream=clientMain.connection.getOutputStream();

            byte[] contents;
            long fileLength = file.length();
            clientMain.writeToServer.println(fileLength);

            long current=0;

            while(current!=fileLength){

                int size = 10000;

                if(fileLength - current >= size)
                    current += size;
                else{

                    size = (int)(fileLength - current);
                    current = fileLength;
                }

                contents = new byte[size];
                bis.read(contents, 0, size);
                outputStream.write(contents);

            }


        }catch (Exception e){

            System.out.println("sending file failed");
            return false;
        }
        return true;
    }


}
