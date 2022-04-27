public class SMessage {

    private String command;
    private String text;

    SMessage(String command,String text){

        this.command=command;
        this.text=text;
        System.out.println(text);

    }

    public String show(){

        String response="online#";
        response+=String.valueOf(serverMain.onlineUsers.size());

        for(WorkerThread workerThread:serverMain.onlineUsers){
            response+="#* "+workerThread.getThreadName();
        }

        return response;

    }

    public void logout(WorkerThread workerThread){

        serverMain.onlineUsers.remove(workerThread);
        workerThread.setLoggedin(false);

    }
}
