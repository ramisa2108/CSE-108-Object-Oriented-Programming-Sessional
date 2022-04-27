import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class serverMain{

    static ArrayList<WorkerThread>onlineUsers=new ArrayList<>();

    public static void main(String[] args) {

        try{

            ServerSocket serverSocket=new ServerSocket(5555);

            while(true){
                Socket connection=serverSocket.accept();
                WorkerThread workerThread=new WorkerThread(connection);
                Thread thread=new Thread(workerThread);
                thread.start();

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

class WorkerThread implements Runnable{

    private String ThreadName;
    private boolean Loggedin;
    private Socket connection;
    private BufferedReader readFromClient;
    private PrintWriter writeToClient;
    private LMessage lMessage;
    private OutputStream outputStream;

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public String getThreadName() {
        return ThreadName;
    }


    public PrintWriter getWriteToClient() {
        return writeToClient;
    }

    public void setLoggedin(boolean loggedin) {
        Loggedin = loggedin;
    }

    WorkerThread(Socket connection){

        this.connection=connection;

        try {
            outputStream=connection.getOutputStream();
            readFromClient = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            writeToClient=new PrintWriter(outputStream,true);

        }catch (Exception e){
            e.printStackTrace();
        }

        Loggedin=false;

    }

    public void run(){

        while(true) {


            String request[];
            String response = "";

            if(connection.isClosed()) {
                break;
            }

            try {
                request = readFromClient.readLine().split("#");

                if (request[0].equalsIgnoreCase("exit")) {
                    break;
                }

                if (request[0].equals("L")) {

                    response = "Login#";
                    lMessage = new LMessage(request[1], request[2],request[3]);

                    if (lMessage.check()) {

                        if (lMessage.loginCheck()) {

                            response += "Already Logged In";

                        } else {

                            Loggedin = true;
                            this.ThreadName = request[1];
                            serverMain.onlineUsers.add(this);
                            response += "Login Successful";

                        }
                    } else response += "Wrong Input";

                } else if (request[0].equals("S")) {

                    SMessage sMessage = new SMessage(request[1], request[2]);

                    if (request[1].equalsIgnoreCase("show")) {
                        response = sMessage.show();

                    }
                    else {

                        sMessage.logout(this);
                        response = "Logged Out";
                    }

                } else if (request[0].equals("B")) {

                    BMessage bMessage = new BMessage(request[1], this);

                    if (lMessage.getType().equalsIgnoreCase("admin")) {

                        bMessage.broadCast();
                        response = "Successfully BroadCasted ";

                    } else {

                        response = "Sorry Broadcasting Failed";
                    }
                }

                else if(request[0].equals("C")){

                    CMessage cMessage=new CMessage(this,request[2],request[3],connection.getInputStream());

                    if(!request[3].equals("null")){

                            int filesize = Integer.parseInt(readFromClient.readLine());
                            if(cMessage.loadFile(filesize))
                                response="Message sent to recipient";
                    }
                    if(!cMessage.findReceiver(request[1])){
                        response="Message could not be sent";
                    }
                    else
                        {
                            cMessage.sendMessage();
                           if(!request[3].equalsIgnoreCase("null")) cMessage.sendFile();
                        response="Message sent to recipient";
                    }

                }

                writeToClient.println(response);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try{

            readFromClient.close();
            writeToClient.close();

            if(Loggedin){

                serverMain.onlineUsers.remove(this);
                Loggedin=false;

            }
            if(connection.isConnected()) connection.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}