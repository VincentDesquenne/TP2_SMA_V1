import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Environnement {
    private String[][] grid;
    private HashMap<Agent, int[]> agents = new HashMap<>();

    public Environnement(int n, int m, int nbA, int nbB, int nbAgents, double k_plus, double k_moins, int pas){
        grid = new String[n][m];
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                grid[i][j] = "0";
            }
        }
        int aGrid = 0;
        int bGrid = 0;
        int agents = 0;
        Random rand = new Random();
        int nAleatoire = 0;
        int mAleatoire = 0;
        while(aGrid != nbA){
            nAleatoire = rand.nextInt(n);
            mAleatoire = rand.nextInt(m);
            if(grid[nAleatoire][mAleatoire] == "0"){
                grid[nAleatoire][mAleatoire] = "A";
                aGrid++;
            }
        }
        while(bGrid != nbB){
            nAleatoire = rand.nextInt(n);
            mAleatoire = rand.nextInt(m);
            if(grid[nAleatoire][mAleatoire] == "0"){
                grid[nAleatoire][mAleatoire] = "B";
                bGrid++;
            }
        }
        while(agents != nbAgents){
            nAleatoire = rand.nextInt(n);
            mAleatoire = rand.nextInt(m);
            if(grid[nAleatoire][mAleatoire] == "0"){
                Agent agent = new Agent(k_plus, k_moins, pas, this);
                int [] tab = new int[2];
                tab[0] = nAleatoire;
                tab[1] = mAleatoire;
                this.agents.put(agent, tab);
                grid[nAleatoire][mAleatoire] = "a";
                agents++;
            }
        }

    }

    public int[] perception(Agent agent, Direction direction){
        int[] coord = this.agents.get(agent);
        this.grid[coord[0]][coord[1]] = "0";
        return coord;
    }

    public void deplacement(Agent agent, int[] coord, Direction direction){
        int[] newCoord = new int[2];
        switch(direction){
            case UPPER_LEFT:
                if(coord[0] >= 1 && coord[1] >= 1){
                    this.grid[coord[0]-1][coord[1]-1] = "a";
                    newCoord[0] = coord[0]-1;
                    newCoord[1] = coord[1]-1;
                }
                break;
            case UPPER:
                if(coord[0] >= 1){
                    this.grid[coord[0]-1][coord[1]] = "a";
                    newCoord[0] = coord[0]-1;
                    newCoord[1] = coord[1];
                }
                break;
            case UPPER_RIGHT:
                if(coord[0] >= 1 && coord[1] <= this.grid[0].length-2){
                    this.grid[coord[0]-1][coord[1]+1] = "a";
                    newCoord[0] = coord[0]-1;
                    newCoord[1] = coord[1]+1;
                }
                break;
            case RIGHT:
                if(coord[1] <= this.grid[0].length-2){
                    this.grid[coord[0]][coord[1]+1] = "a";
                    newCoord[0] = coord[0];
                    newCoord[1] = coord[1]+1;
                }
                break;
            case LOWER_RIGHT:
                if(coord[0] <= this.grid.length-2 && coord[1] <= this.grid[0].length-2){
                    this.grid[coord[0]+1][coord[1]+1] = "a";
                    newCoord[0] = coord[0]+1;
                    newCoord[1] = coord[1]+1;
                }
                break;
            case LOWER:
                if(coord[0] <= this.grid.length-2){
                    this.grid[coord[0]+1][coord[1]] = "a";
                    newCoord[0] = coord[0]+1;
                    newCoord[1] = coord[1];
                }
                break;
            case LOWER_LEFT:
                if(coord[0] <= this.grid.length-2 && coord[1] >= 1){
                    this.grid[coord[0]+1][coord[1]-1] = "a";
                    newCoord[0] = coord[0]+1;
                    newCoord[1] = coord[1]-1;
                }
                break;
            case LEFT:
                if(coord[1] >= 1){
                    this.grid[coord[0]][coord[1]-1] = "a";
                    newCoord[0] = coord[0];
                    newCoord[1] = coord[1]-1;
                }
                break;
        }
        this.agents.put(agent, newCoord);

    }

    @Override
    public String toString() {
        String res = "Environnement :";
        for(int i=0; i<grid.length; i++){
            res+= "\n";
            for(int j=0; j<grid[0].length; j++){
                res+= grid[i][j] + " ";
            }
        }
        return res;
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
