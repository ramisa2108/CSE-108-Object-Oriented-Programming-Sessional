public class BMessage {

    private String text;
    private WorkerThread sender;

    BMessage(String text,WorkerThread sender){

        this.sender=sender;
        this.text=text;
    }

    public void broadCast(){

        for(WorkerThread workerThread:serverMain.onlineUsers){

            if(workerThread.equals(sender))
                continue;

            else{
                workerThread.getWriteToClient().println("broadcast#"+sender.getThreadName()+": "+text);
            }

        }
    }
}
