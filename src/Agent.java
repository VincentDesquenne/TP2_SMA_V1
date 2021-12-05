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

    public void action() { //action de l'agent
        Random rand = new Random();
        for(int i=0; i<pas;i++){
            ArrayList<Direction> directions = this.environnement.perceptionSeDeplacer(this); //appel à l'environnement
            int dir = rand.nextInt(directions.size());
            this.deplacer(directions.get(dir)); //déplacement
        }
        if (this.object == "") { //action prise ou dépot selon objet porté
            this.prise();
        } else {
            this.depot();
        }
    }

    public void deplacer(Direction direction) { //déplacement
        this.environnement.deplacement(this, direction);

    }

    public void prise() { //action prise
        String object = this.environnement.perceptionPrise(this); //appel à l'environnement
        if (object != "0") { //si la case contient un objet
            double f = this.calculerF(object); //calcul de F
            double pPrise = Math.pow(this.k_plus / (this.k_plus + f), 2); //calcul de la probabilité de prise
            if (Math.random() <= pPrise) {
                this.object = object;
                this.environnement.prise(this); //action de prise de l'objet
            }
        }
    }

    public void depot() { //action depot
        String object = this.environnement.perceptionDepot(this); //appel à l'environnement
        if (object == "0") { //si la case ne contient pas d'objet
            double f = this.calculerF(this.object); //calcul de F
            double pDepot = Math.pow(f / (this.k_moins + f), 2); //calcul de la probabilité de dépot
            if (Math.random() <= pDepot) {
                this.environnement.depot(this, this.object); //action de dépot
                this.object = "";
            }
        }
    }

    public double calculerF(String object) { //calcul de F
        int nbObj = 0;
        int nbObjOpp = 0;
        for (int i = 0; i < memoire.size(); i++) { //calcul de la fréquence de l'objet en paramètre dans la mémoire
            if (memoire.toArray()[i] == object) {
                nbObj++;
            } else if (memoire.toArray()[i] != "0") {
                nbObjOpp++; //calcul de la fréquence de l'objet opposé en paramètre dans la mémoire pour ajouter le taux d'erreur
            }
        }
        double res = (double) nbObj + (double) nbObjOpp * this.environnement.getTauxErreur();
        return res / (double) memoire.size();
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

    public void ajouterMemoire(String objet) { //méthode d'ajout à la mémoire
        if (this.memoire.size() == this.tailleMemoire) {
            this.memoire.removeFirst(); //si la taille de la mémoire est maximale, on enlève la plus ancienne case parcourue de la mémoire
        }
        this.memoire.addLast(objet); //on ajoute la nouvelle case à la mémoire
    }
}
