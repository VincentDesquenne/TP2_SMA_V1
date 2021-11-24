import java.awt.*;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Environnement extends JPanel{
    private String[][] grid;
    private HashMap<Agent, int[]> agents = new HashMap<>();

    public Environnement(int n, int m, int nbA, int nbB, int nbAgents, double k_plus, double k_moins, int taille, int pas) {
        grid = new String[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                grid[i][j] = "0";
            }
        }
        int aGrid = 0;
        int bGrid = 0;
        int agents = 0;
        Random rand = new Random();
        int nAleatoire = 0;
        int mAleatoire = 0;
        while (aGrid != nbA) {
            nAleatoire = rand.nextInt(n);
            mAleatoire = rand.nextInt(m);
            if (grid[nAleatoire][mAleatoire] == "0") {
                grid[nAleatoire][mAleatoire] = "A";
                aGrid++;
            }
        }
        while (bGrid != nbB) {
            nAleatoire = rand.nextInt(n);
            mAleatoire = rand.nextInt(m);
            if (grid[nAleatoire][mAleatoire] == "0") {
                grid[nAleatoire][mAleatoire] = "B";
                bGrid++;
            }
        }
        while (agents != nbAgents) {
            nAleatoire = rand.nextInt(n);
            mAleatoire = rand.nextInt(m);
            if (grid[nAleatoire][mAleatoire] == "0") {
                Agent agent = new Agent(k_plus, k_moins, pas, taille, this);
                int[] tab = new int[2];
                tab[0] = nAleatoire;
                tab[1] = mAleatoire;
                this.agents.put(agent, tab);
                agents++;
            }
        }

    }

    public ArrayList<Direction> perceptionSeDeplacer(Agent agent, int pas) {
        int[] coord = this.agents.get(agent);
        //this.grid[coord[0]][coord[1]] = "0";
        return this.possibleMoves(agent, coord, pas);
    }

    public String perceptionPrise(Agent agent) {
        int[] coord = this.agents.get(agent);
        return this.grid[coord[0]][coord[1]];
    }

    public String perceptionDepot(Agent agent) {
        int[] coord = this.agents.get(agent);
        return this.grid[coord[0]][coord[1]];
    }

    public ArrayList<Direction> possibleMoves(Agent agent, int[] coord, int pas) {
        ArrayList<Direction> directions = new ArrayList<>();
        if (coord[0] >= pas) {
            directions.add(Direction.UPPER);
            if (coord[1] >= pas) {
                directions.add(Direction.UPPER_LEFT);
                directions.add(Direction.LEFT);
            }
            if (coord[1] <= this.grid[0].length - (pas + 1)) {
                directions.add(Direction.UPPER_RIGHT);
                directions.add(Direction.RIGHT);
            }
        }
        if (coord[0] <= this.grid.length - (pas + 1)) {
            directions.add(Direction.LOWER);
            if (coord[1] <= this.grid[0].length - (pas + 1)) {
                directions.add(Direction.LOWER_RIGHT);
            }
            if (coord[1] >= pas) {
                directions.add(Direction.LOWER_LEFT);
            }
        }
        return directions;
    }

    public void prise(Agent agent) {
        int[] coord = this.agents.get(agent);
        this.grid[coord[0]][coord[1]] = "0";
    }

    public void depot(Agent agent, String object) {
        int[] coord = this.agents.get(agent);
        this.grid[coord[0]][coord[1]] = object;
    }

    public void deplacement(Agent agent, Direction direction, int pas) {
        int[] coord = this.agents.get(agent);
        agent.ajouterMemoire(this.grid[coord[0]][coord[1]]);
        int[] newCoord = new int[2];
        switch (direction) {
            case UPPER_LEFT:
                newCoord[0] = coord[0] - pas;
                newCoord[1] = coord[1] - pas;
                break;
            case UPPER:
                newCoord[0] = coord[0] - pas;
                newCoord[1] = coord[1];
                break;
            case UPPER_RIGHT:
                newCoord[0] = coord[0] - pas;
                newCoord[1] = coord[1] + pas;
                break;
            case RIGHT:
                newCoord[0] = coord[0];
                newCoord[1] = coord[1] + pas;
                break;
            case LOWER_RIGHT:
                newCoord[0] = coord[0] + pas;
                newCoord[1] = coord[1] + pas;
                break;
            case LOWER:
                newCoord[0] = coord[0] + pas;
                newCoord[1] = coord[1];
                break;
            case LOWER_LEFT:
                newCoord[0] = coord[0] + pas;
                newCoord[1] = coord[1] - pas;
                break;
            case LEFT:
                newCoord[0] = coord[0];
                newCoord[1] = coord[1] - pas;
                break;
        }
        this.agents.get(agent)[0] = newCoord[0];
        this.agents.get(agent)[1] = newCoord[1];

    }

    @Override
    public String toString() {
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


    public String toString(int[] coord) {
        String res = "Environnement :";
        int[] tab = new int[2];
        boolean agentHere = false;
        boolean agentActif = false;
        for (int i = 0; i < grid.length; i++) {
            res += "\n";
            for (int j = 0; j < grid[0].length; j++) {
                agentHere = false;
                agentActif = false;
                tab[0] = i;
                tab[1] = j;
                for(int k=0; k<this.agents.values().size(); k++){
                    if(Arrays.equals(coord, tab)){
                        agentActif = true;
                        break;
                    }
                    if(Arrays.equals((int[])this.agents.values().toArray()[k],tab)){
                        agentHere = true;
                        break;
                    }
                }
                if(agentActif){
                    res+= grid[i][j] + "a* ";
                }
                else if(agentHere){
                    res+= grid[i][j] + "a ";
                } else {
                    res += grid[i][j] + "  ";
                }
            }
        }
        return res;
    }

    public void paintComponent(Graphics g) {
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
            }
        }
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
}
