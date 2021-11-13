import java.util.Collections;
import java.util.Random;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        Environnement env = new Environnement(50, 50, 200, 200, 20, 0.1, 0.3, 10, 1);
        System.out.println(env);
        Set<Agent> agents = env.getAgents().keySet();
        Agent[] agentsTab = new Agent[agents.size()];
        agents.toArray(agentsTab);
        Random rand = new Random();
        for(int i=0; i<100000; i++){
            Agent agent = agentsTab[rand.nextInt(agentsTab.length)];
            agent.action();
            System.out.println(env);
        }

    }
}
