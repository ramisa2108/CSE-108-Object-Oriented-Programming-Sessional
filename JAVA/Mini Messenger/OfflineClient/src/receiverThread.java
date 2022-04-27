
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

class receiverThread implements Runnable{

    Socket connection;
    BufferedReader readFromServer;
    String message;

    public String getMessage() {
        return message;
    }


    receiverThread(Socket connection){
        try {

            this.message="null";
            this.connection = connection;
            readFromServer = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        }
        catch (Exception e){
            System.out.println("Problem in receiverThread constructor");
        }
    }
    public void close(){
        try{
            readFromServer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void run() {

        String components[];

        while(true){
            if(connection.isClosed()) break;

            try {

                message = readFromServer.readLine();
                components = message.split("#");

                if (components[0].equalsIgnoreCase("Login")) {
                    message = components[1];
                    if (message.equalsIgnoreCase("Login Successful")) {

                        clientMain.loginStatus = true;
                    }
                } else if (components[0].equalsIgnoreCase("Logged Out")) {
                    clientMain.loginStatus = false;
                    clientMain.personalMessages.clear();
                    clientMain.broadcastedMessages.clear();
                    clientMain.showActive.clear();
                } else if (components[0].equalsIgnoreCase("online")) {
                    clientMain.showActive.clear();

                    for (int i = 0; i < Integer.parseInt(components[1]); i++) {
                        clientMain.showActive.add(components[i + 2]);
                    }
                } else if (components[0].equalsIgnoreCase("broadcast")) {

                    clientMain.broadcastedMessages.add(components[1]);

                } else if (components[0].equalsIgnoreCase("Sent")) {

                    String addmessage = components[1];
                    if (components.length > 2) {
                        addmessage += "\n" + components[2];
                    }
                    clientMain.personalMessages.add(addmessage);

                } else if (components[0].equalsIgnoreCase("File")) {
                    synchronized (this) {
                        int filesize = Integer.parseInt(components[2]);
                        String fileName = components[1];

                        byte[] contents = new byte[10000];
                        try {

                            FileOutputStream fos = new FileOutputStream("ReceiverCopy" + fileName);
                            BufferedOutputStream bos = new BufferedOutputStream(fos);

                            int bytesRead = 0;
                            int total = 0;

                            while (total != filesize) {
                                bytesRead = connection.getInputStream().read(contents);
                                total += bytesRead;
                                bos.write(contents, 0, bytesRead);
                            }
                            bos.flush();

                        } catch (Exception e) {
                            System.out.println("Unable to load file");
                        }
                    }
                }

                }catch(Exception e){
                    System.out.println("Problem in reading " + message);
                }

        }

    }
}