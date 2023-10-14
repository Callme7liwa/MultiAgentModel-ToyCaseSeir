import Services.MultiAgentService;
import Services.MultiAgentServiceImpl;

public class Main {
    public static  void main(String args[]){
        MultiAgentService multiAgentService = MultiAgentServiceImpl.getInstance();
        multiAgentService.start();
    }
}
