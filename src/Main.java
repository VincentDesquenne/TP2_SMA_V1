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

        while(true){
            try {
                System.out.println("Entrer la largeur et la longueur de la grille");
                n = reader.nextInt();
                break;
            } catch(InputMismatchException e){
                System.out.println("Format non valide");
                reader.nextLine();
            }
        }

        while (true){
            try {
                System.out.println("Entrer le nombre d'objets A et B");
                n2 = reader.nextInt();
                break;
            }catch(InputMismatchException e){
                System.out.println("Format non valide");
                reader.nextLine();
            }
        }

        while(true){
            try {
                System.out.println("Entrer le nombre d'agents");
                nbAgents = reader.nextInt();
                break;
            } catch(InputMismatchException e){
                System.out.println("Format non valide");
                reader.nextLine();
            }
        }

        while(true){
            try {
                System.out.println("Entrer le nombre de pas");
                nbPas = reader.nextInt();
                break;
            } catch(InputMismatchException e){
                System.out.println("Format non valide");
                reader.nextLine();
            }
        }

        while(true){
            try {
                System.out.println("Entrer la valeur de k plus (avec virgule)");
                k_plus = reader.nextDouble();
                break;
            } catch(InputMismatchException e){
                System.out.println("Format non valide");
                reader.nextLine();
            }
        }

        while(true){
            try {
                System.out.println("Entrer la valeur de k moins (avec virgule)");
                k_moins = reader.nextDouble();
                break;
            } catch(InputMismatchException e){
                System.out.println("Format non valide");
                reader.nextLine();
            }
        }

        while(true){
            try {
                System.out.println("Entrer le taux d'erreur (avec virgule)");
                tauxErreur = reader.nextDouble();
                break;
            } catch(InputMismatchException e){
                System.out.println("Format non valide");
                reader.nextLine();
            }
        }

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



        Environnement env = new Environnement(n, n, n2, n2, nbAgents, k_plus, k_moins, 10, nbPas,tauxErreur);
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
