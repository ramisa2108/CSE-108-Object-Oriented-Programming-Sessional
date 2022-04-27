import java.io.*;

public class CMessage {

    WorkerThread sender;
    WorkerThread reciever;
    String message;
    String fileName;
    InputStream inputStream;

    CMessage(WorkerThread sender,String message,String fileName,InputStream inputStream ){

        this.sender=sender;
        this.message=message;
        this.fileName=fileName;
        this.inputStream=inputStream;

    }
    public boolean findReceiver(String name){

        for(WorkerThread workerThread:serverMain.onlineUsers){

            if(workerThread.getThreadName().equals(name)){
                reciever=workerThread;
                return true;
            }

        }
        return false;
    }

    synchronized public boolean loadFile(int filesize){

        byte[] contents = new byte[10000];
        try {

            FileOutputStream fos = new FileOutputStream("Copy"+fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            int bytesRead =0;
            int total =0;

            while(total!=filesize)
            {
                bytesRead=inputStream.read(contents);
                total+=bytesRead;
                bos.write(contents, 0, bytesRead);
            }
            bos.flush();

        }catch (Exception e){
            System.out.println("Unable to load file");
            return false;
        }


        return true;

    }
    public void sendMessage(){

        String send="Sent#"+sender.getThreadName()+": "+message;
        if(!fileName.equals("null")){
            send+="#"+"File: "+fileName;
        }
        reciever.getWriteToClient().println(send);
    }
    synchronized public void sendFile(){
        try {

            File file = new File("Copy"+fileName);
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            OutputStream outputStream=reciever.getOutputStream();

            byte[] contents;
            long fileLength = file.length();
            reciever.getWriteToClient().println("File#"+fileName+"#"+String.valueOf(fileLength));
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


        }catch (Exception e) {

            e.printStackTrace();
        }

    }
}
