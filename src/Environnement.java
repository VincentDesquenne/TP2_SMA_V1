import java.awt.*;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Environnement extends JPanel{
    private String[][] grid;
    private HashMap<Agent, int[]> agents = new HashMap<>();
    private double tauxErreur;
    private HashMap<Agent, Agent> agentsObjetC = new HashMap<>();
    private boolean[][] marquage;
    private String[][] gridAgent;

    public Environnement(int n, int m, int nbA, int nbB, int nbC, int nbAgents, double k_plus, double k_moins, int taille, int pas, double tauxErreur, int dSignal) {
        grid = new String[n][m];
        marquage = new boolean[n][m];
        gridAgent = new String[n][m];
        for (int i = 0; i < n; i++) { //initialisation de la grille
            for (int j = 0; j < m; j++) {
                grid[i][j] = "0";
                marquage[i][j] = false;
                gridAgent[i][j] = "0";
            }
        }
        this.tauxErreur = tauxErreur;
        int aGrid = 0;
        int bGrid = 0;
        int cGrid = 0;
        int agents = 0;
        Random rand = new Random();
        int nAleatoire = 0;
        int mAleatoire = 0;
        while (aGrid != nbA) { //remplissage de la grille avec les objets A
            nAleatoire = rand.nextInt(n);
            mAleatoire = rand.nextInt(m);
            if (grid[nAleatoire][mAleatoire] == "0") {
                grid[nAleatoire][mAleatoire] = "A";
                aGrid++;
            }
        }
        while (bGrid != nbB) { //remplissage de la grille avec les objets B
            nAleatoire = rand.nextInt(n);
            mAleatoire = rand.nextInt(m);
            if (grid[nAleatoire][mAleatoire] == "0") {
                grid[nAleatoire][mAleatoire] = "B";
                bGrid++;
            }
        }
        while(cGrid != nbC) {
            nAleatoire = rand.nextInt(n);
            mAleatoire = rand.nextInt(m);
            if (grid[nAleatoire][mAleatoire] == "0") {
                grid[nAleatoire][mAleatoire] = "C";
                cGrid++;
            }
        }
        while (agents != nbAgents) { //remplissage de la grille avec les agents
            nAleatoire = rand.nextInt(n);
            mAleatoire = rand.nextInt(m);
            if (grid[nAleatoire][mAleatoire] == "0") {
                Agent agent = new Agent(k_plus, k_moins, pas, taille, dSignal, this);
                int[] tab = new int[2];
                tab[0] = nAleatoire;
                tab[1] = mAleatoire;
                gridAgent[nAleatoire][mAleatoire] = "A";
                this.agents.put(agent, tab);
                agents++;
            }
        }

    }

    public ArrayList<Direction> perceptionSeDeplacer(Agent agent) { //réponse de l'environnement à l'agent pour le déplacement
        int[] coord = this.agents.get(agent);
        //this.grid[coord[0]][coord[1]] = "0";
        return this.possibleMoves(agent, coord); //renvoie les déplacements possibles
    }

    public String perceptionPrise(Agent agent) { //réponse de l'environnement à l'agent pour la prise d'objet
        int[] coord = this.agents.get(agent);
        return this.grid[coord[0]][coord[1]]; //renvoie l'objet présent sur la case de l'agent
    }

    public String perceptionDepot(Agent agent) { //réponse de l'environnement à l'agent pour le dépôt d'objet
        int[] coord = this.agents.get(agent);
        return this.grid[coord[0]][coord[1]]; //renvoie l'objet présent sur la case de l'agent
    }

    public ArrayList<Direction> possibleMoves(Agent agent, int[] coord) { //fonctions qui détermine les déplacements possibles de l'agent
        ArrayList<Direction> directions = new ArrayList<>();
        if (coord[0] >= 1) {
            directions.add(Direction.UPPER);
            if (coord[1] >= 1) {
                directions.add(Direction.UPPER_LEFT);
                directions.add(Direction.LEFT);
            }
            if (coord[1] <= this.grid[0].length - 2) {
                directions.add(Direction.UPPER_RIGHT);
                directions.add(Direction.RIGHT);
            }
        }
        if (coord[0] <= this.grid.length - 2) {
            directions.add(Direction.LOWER);
            if (coord[1] <= this.grid[0].length - 2) {
                directions.add(Direction.LOWER_RIGHT);
            }
            if (coord[1] >= 1) {
                directions.add(Direction.LOWER_LEFT);
            }
        }
        return directions;
    }

    public void prise(Agent agent) { //modification de la grille après action prise de l'agent
        int[] coord = this.agents.get(agent);
        this.grid[coord[0]][coord[1]] = "0";
    }

    public void depot(Agent agent, String object) { //modification de la grille après action dépôt de l'agent
        int[] coord = this.agents.get(agent);
        this.grid[coord[0]][coord[1]] = object;
    }

    public void deplacement(Agent agent, Direction direction) { //modification de la grille après déplacement de l'agent
        int[] coord = this.agents.get(agent);
        agent.ajouterMemoire(this.grid[coord[0]][coord[1]]); //ajout de la case précédente à la mémoire de l'agent
        int[] newCoord = new int[2];
        switch (direction) {
            case UPPER_LEFT:
                newCoord[0] = coord[0] - 1;
                newCoord[1] = coord[1] - 1;
                break;
            case UPPER:
                newCoord[0] = coord[0] - 1;
                newCoord[1] = coord[1];
                break;
            case UPPER_RIGHT:
                newCoord[0] = coord[0] - 1;
                newCoord[1] = coord[1] + 1;
                break;
            case RIGHT:
                newCoord[0] = coord[0];
                newCoord[1] = coord[1] + 1;
                break;
            case LOWER_RIGHT:
                newCoord[0] = coord[0] + 1;
                newCoord[1] = coord[1] + 1;
                break;
            case LOWER:
                newCoord[0] = coord[0] + 1;
                newCoord[1] = coord[1];
                break;
            case LOWER_LEFT:
                newCoord[0] = coord[0] + 1;
                newCoord[1] = coord[1] - 1;
                break;
            case LEFT:
                newCoord[0] = coord[0];
                newCoord[1] = coord[1] - 1;
                break;
        }
        this.gridAgent[coord[0]][coord[1]] = "0";
        this.gridAgent[newCoord[0]][newCoord[1]] = "A";
        this.agents.get(agent)[0] = newCoord[0];
        this.agents.get(agent)[1] = newCoord[1];

    }

    public void cheminVersSignal(Agent agent, int[] coordSignal) { //modification de la grille après déplacement de l'agent
        int[] coord = this.agents.get(agent);
        if (coord[0] < coordSignal[0]) {
            if(coord[1] < coordSignal[1]){
                this.deplacement(agent, Direction.LOWER_RIGHT);
            } else if(coord[1] > coordSignal[1]){
                this.deplacement(agent, Direction.LOWER_LEFT);
            } else {
                this.deplacement(agent, Direction.LOWER);
            }
        } else if (coord[0] > coordSignal[0]) {
            if(coord[1] < coordSignal[1]){
                this.deplacement(agent, Direction.UPPER_RIGHT);
            } else if(coord[1] > coordSignal[1]){
                this.deplacement(agent, Direction.UPPER_LEFT);
            } else {
                this.deplacement(agent, Direction.UPPER);
            }
        } else {
            if(coord[1] < coordSignal[1]){
                this.deplacement(agent, Direction.RIGHT);
            } else if(coord[1] > coordSignal[1]){
                this.deplacement(agent, Direction.LEFT);
            }
        }
    }

    public int[] perceptionPos(Agent agent){
        int[] coord = this.agents.get(agent);
        return coord;
    }


    @Override
    public String toString() { //affichage de la grille
        String res = "Environnement :";
        int[] tab = new int[2];
        boolean agentHere = false;
        for (int i = 0; i < grid.length; i++) {
            res += "\n";
            for (int j = 0; j < grid[0].length; j++) {
                agentHere = false;
                tab[0] = i;
                tab[1] = j;
                for(int k=0; k<this.agents.values().size(); k++){
                    if(Arrays.equals((int[])this.agents.values().toArray()[k],tab)){
                        agentHere = true;
                        break;
                    }
                }
                if(agentHere){
                    res+= grid[i][j] + "a ";
                } else {
                    res += grid[i][j] + "  ";
                }
            }
        }
        return res;
    }

    public void paintComponent(Graphics g) { //interface graphique
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(4));
        for (int i = 0; i < grid.length; i++) {
            for(int j=0; j < grid[i].length; j++){
                Dimension size = getSize();
                int w = size.width ;
                int h = size.height;
                if(grid[i][j] == "A"){
                    g2d.setColor(Color.red);
                    int x = j * w / grid.length;
                    int y = i * h / grid[0].length;
                    g2d.drawLine(x, y, x, y);
                }
                else if(grid[i][j] == "B"){
                    g2d.setColor(Color.blue);
                    int x = j * w / grid.length;
                    int y = i * h / grid[0].length;
                    g2d.drawLine(x, y, x, y);
                }
                else if(grid[i][j] == "C"){
                    g2d.setColor(Color.green);
                    int x = j * w / grid.length;
                    int y = i * h / grid[0].length;
                    g2d.drawLine(x, y, x, y);
                }
            }
        }
    }

    public boolean signal(Agent agent, int dSignal){
        int[] coord = this.agents.get(agent);
        int intensite = 1 - 1/dSignal;
        for(int i=1; i<=dSignal; i++){
            for(Agent a : agents.keySet()){
                if(a != agent) {
                    if (this.agents.get(a)[0] + i == coord[0] || this.agents.get(a)[0] - i == coord[0] || this.agents.get(a)[0] == coord[0]) {
                        if (this.agents.get(a)[1] + i == coord[1] || this.agents.get(a)[1] - i == coord[1] || this.agents.get(a)[1] == coord[1]) {
                            //if(Math.random() < intensite) {
                                a.setSignalRecu(true);
                                a.setCoordSignal(coord);
                                agentsObjetC.put(agent, a);
                                agentsObjetC.put(a, agent);
                                return true;
                            //}
                        }
                    }
                }
            }
            intensite = intensite - 1/dSignal;
        }
        return false;
    }


    public String[][] getGrid() {
        return grid;
    }

    public void setGrid(String[][] grid) {
        this.grid = grid;
    }

    public HashMap<Agent, int[]> getAgents() {
        return agents;
    }

    public void setAgents(HashMap<Agent, int[]> agents) {
        this.agents = agents;
    }

    public double getTauxErreur() {
        return tauxErreur;
    }

    public void setTauxErreur(double tauxErreur) {
        this.tauxErreur = tauxErreur;
    }

    public HashMap<Agent, Agent> getAgentsObjetC() {
        return agentsObjetC;
    }

    public void setAgentsObjetC(HashMap<Agent, Agent> agentsObjetC) {
        this.agentsObjetC = agentsObjetC;
    }

    public boolean[][] getMarquage() {
        return marquage;
    }

    public boolean getMarquageAgent(Agent agent) {
        int[] coord = this.agents.get(agent);
        return marquage[coord[0]][coord[1]];
    }

    public void setMarquageAgent(Agent agent, boolean etat) {
        int[] coord = this.agents.get(agent);
        marquage[coord[0]][coord[1]] = etat;
    }

    public void setMarquage(boolean[][] marquage) {
        this.marquage = marquage;
    }
}
