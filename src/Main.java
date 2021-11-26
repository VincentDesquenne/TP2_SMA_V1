import javax.swing.*;
import java.util.Collections;
import java.util.Random;
import java.util.Set;


public class Main extends JPanel {
    public static void main(String[] args) {

        Environnement env = new Environnement(50, 50, 200, 200, 20, 0.1, 0.3, 15, 1,0.0);
        System.out.println(env);
        JFrame frame = new JFrame("Grille");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(env);
        frame.setSize(250, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Set<Agent> agents = env.getAgents().keySet();
        Agent[] agentsTab = new Agent[agents.size()];
        agents.toArray(agentsTab);
        Random rand = new Random();
        for(int i=0; i<500000; i++){
            for(int j=0; j<agentsTab.length; j++){
                Agent agent = agentsTab[j];
                agent.action();
            }
        }
        System.out.println(env);


    }
}
