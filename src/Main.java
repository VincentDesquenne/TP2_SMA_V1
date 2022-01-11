import javax.swing.*;
import java.util.*;


public class Main extends JPanel {
    public static void main(String[] args) {

        Scanner reader = new Scanner(System.in);  // Reading from System.in

        int n = 50;
        int n2 = 200;
        int nbAgents = 20;
        int nbPas = 1;
        double k_plus = 0.1;
        double k_moins = 0.3;
        double tauxErreur = 0.0;
        int nbIterations = 1000000;
        int dSignal = 4;
        double r = 0.1;

        while(true){
            try {
                System.out.println("Entrer le nombre d'itérations");
                nbIterations = reader.nextInt();
                break;
            } catch(InputMismatchException e){
                System.out.println("Format non valide");
                reader.nextLine();
            }
        }
        while(true){
            try {
                System.out.println("Entrer la distance du signal");
                dSignal = reader.nextInt();
                break;
            } catch(InputMismatchException e){
                System.out.println("Format non valide");
                reader.nextLine();
            }
        }
        while(true){
            try {
                System.out.println("Entrer le taux r (avec virgule)");
                r = reader.nextDouble();
                break;
            } catch(InputMismatchException e){
                System.out.println("Format non valide");
                reader.nextLine();
            }
        }



        Environnement env = new Environnement(n, n, n2, n2, n2,  nbAgents, k_plus, k_moins, 10, nbPas,tauxErreur, dSignal, r, 1);
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
            env.evaporation();
        }
        System.out.println(env);
    }
}
