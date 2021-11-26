import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Agent {
    private double k_plus;
    private double k_moins;
    private int pas;
    private Environnement environnement;
    private String object;
    private ArrayDeque<String> memoire;
    private int tailleMemoire;

    public Agent(double k_plus, double k_moins, int pas, int taille, Environnement environnement) {
        this.k_plus = k_plus;
        this.k_moins = k_moins;
        this.pas = pas;
        this.environnement = environnement;
        this.object = "";
        this.memoire = new ArrayDeque<>();
        this.tailleMemoire = taille;
    }

    public void action() {
        Random rand = new Random();
        ArrayList<Direction> directions = this.environnement.perceptionSeDeplacer(this, pas);
        int dir = rand.nextInt(directions.size());
        this.deplacer(directions.get(dir));
        if (this.object == "") {
            this.prise();
        } else {
            this.depot();
        }
    }

    public void deplacer(Direction direction) {
        this.environnement.deplacement(this, direction, pas);
    }

    public void prise() {
        String object = this.environnement.perceptionPrise(this);
        if (object != "0") {
            double f = this.calculerF(object);
            double pPrise = Math.pow(this.k_plus / (this.k_plus + f), 2);
            if (Math.random() <= pPrise) {
                this.object = object;
                this.environnement.prise(this);
            }
        }
    }

    public void depot() {
        String object = this.environnement.perceptionDepot(this);
        if (object == "0") {
            double f = this.calculerF(this.object);
            double pDepot = Math.pow(f / (this.k_moins + f), 2);
            if (Math.random() <= pDepot) {
                this.environnement.depot(this, this.object);
                this.object = "";
            }
        }
    }

    public double calculerF(String object) {
        int nbObj = 0;
        int nbObjOpp = 0;
        for (int i = 0; i < memoire.size(); i++) {
            if (memoire.toArray()[i] == object) {
                nbObj++;
            }
            else if(memoire.toArray()[i] != "0"){
                nbObjOpp++;
            }
        }
        double res = (double) nbObj +  (double) nbObjOpp*this.environnement.getTauxErreur();
        return (double) nbObj / (double) memoire.size();
    }


    public double getK_plus() {
        return k_plus;
    }

    public void setK_plus(double k_plus) {
        this.k_plus = k_plus;
    }

    public double getK_moins() {
        return k_moins;
    }

    public void setK_moins(double k_moins) {
        this.k_moins = k_moins;
    }

    public int getPas() {
        return pas;
    }

    public void setPas(int pas) {
        this.pas = pas;
    }

    public void ajouterMemoire(String objet) {
        if (this.memoire.size() == this.tailleMemoire) {
            this.memoire.removeFirst();
        }
        this.memoire.addLast(objet);
    }
}
