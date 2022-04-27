import java.io.File;
import java.util.Scanner;

public class LMessage {

    private String Name;
    private String password;
    private String type;

    LMessage(String Name,String password,String type){

        this.Name=Name;
        this.password=password;
        this.type=type;

    }

    public String getType() {
        return type;
    }

    public boolean check() throws Exception{

        boolean result=false;
        File file=new File("src\\allUsers");
        Scanner scanner=new Scanner(file);

        while(scanner.hasNextLine()) {

            String Name = scanner.nextLine();
            String password = scanner.nextLine();
            String type = scanner.nextLine();

            if(this.Name.equals(Name) && this.password.equals(password) && this.type.equals(type) ){
                result=true;
                break;
            }

        }
        return result;
    }

    public boolean loginCheck(){

        for(WorkerThread workerThread:serverMain.onlineUsers){

            if(workerThread.getThreadName().equalsIgnoreCase(this.Name)){
                return true;
            }

        }
        return false;

    }
}
