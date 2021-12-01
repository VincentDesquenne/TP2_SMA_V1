import javax.swing.*;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;


public class Main extends JPanel {
    public static void main(String[] args) {

        Scanner reader = new Scanner(System.in);  // Reading from System.in

        System.out.println("Entrer la largeur et la longueur de la grille");
        int n = reader.nextInt();
        System.out.println("Entrer le nombre d'objets A et B");
        int n2 = reader.nextInt();
        System.out.println("Entrer le nombre d'agents");
        int nbAgents = reader.nextInt();
        System.out.println("Entrer la valeur de k plus (avec virgule)");
        double k_plus = reader.nextDouble();
        System.out.println("Entrer la valeur de k moins (avec virgule)");
        double k_moins = reader.nextDouble();
        System.out.println("Entrer le taux d'erreur (avec virgule)");
        double tauxErreur = reader.nextDouble();
        System.out.println("Entrer le nombre d'itérations");
        int nbIterations = reader.nextInt();
        Environnement env = new Environnement(n, n, n2, n2, nbAgents, k_plus, k_moins, 10, 1,tauxErreur);
        System.out.println(env);
        JFrame frame = new JFrame("Grille");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(env);
        frame.setSize(450, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Set<Agent> agents = env.getAgents().keySet();
        Agent[] agentsTab = new Agent[agents.size()];
        agents.toArray(agentsTab);
        Random rand = new Random();
        for(int i=0; i<nbIterations; i++){
            for(int j=0; j<agentsTab.length; j++){
                Agent agent = agentsTab[j];
                agent.action();
                frame.repaint();
            }
        }
        System.out.println(env);


    }
}
