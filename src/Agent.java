import java.lang.reflect.Array;
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
    private int dSignal;
    private boolean signalRecu;
    private int[] coordSignal;
    private boolean signalEmis;
    private boolean attendre;

    public Agent(double k_plus, double k_moins, int pas, int taille, int dSignal, Environnement environnement) {
        this.k_plus = k_plus;
        this.k_moins = k_moins;
        this.pas = pas;
        this.environnement = environnement;
        this.object = "";
        this.memoire = new ArrayDeque<>();
        this.tailleMemoire = taille;
        this.dSignal = dSignal;
        this.signalEmis = false;
        this.signalRecu = false;
        this.attendre = false;
    }

    public void action() { //action de l'agent
        Random rand = new Random();
        for (int i = 0; i < pas; i++) {
            if (!attendre) {
                ArrayList<Direction> directions = this.environnement.perceptionSeDeplacer(this); //appel à l'environnement
                if (this.environnement.getPheromoneAgent(this) == 0.0 || this.object != "") {
                    int dir = rand.nextInt(directions.size());
                    this.deplacer(directions.get(dir)); //déplacement
                } else {
                    if(Math.random() <= this.environnement.getPheromoneAgent(this)){
                        this.deplacerVersSignal(directions);
                    } else {
                        int dir = rand.nextInt(directions.size());
                        this.deplacer(directions.get(dir));
                    }
                }
            }
        }
        if (this.object == "") { //action prise ou dépot selon objet porté
            this.prise();
        } else {
            this.depot();
        }

        if (signalEmis) {
            if (this.environnement.getPheromoneAgent(this) < 0.10) {
                this.environnement.setMarquageAgent(this, false);
                this.attendre = false;
                this.environnement.stopSignal(this, dSignal);
            }
        }




    }

    public void deplacer(Direction direction) { //déplacement
        this.environnement.deplacement(this, direction);
    }

    public void deplacerVersSignal(ArrayList<Direction> directions) { //déplacement vers origine du signal
        this.environnement.cheminVersSignal(this, directions);
    }

    public void prise() { //action prise
        String object = this.environnement.perceptionPrise(this); //appel à l'environnement
        if (object != "0") { //si la case contient un objet
            if (object != "C") { //si cet objet n'est pas un objet C
                double f = this.calculerFV2(object); //calcul de F
                double pPrise = Math.pow(this.k_plus / (this.k_plus + f), 2); //calcul de la probabilité de prise
                if (Math.random() <= pPrise) {
                    this.object = object;
                    this.environnement.prise(this); //action de prise de l'objet
                }
            } else { //si cet objet est un objet C
                if (this.environnement.getMarquageAgent(this)) { //si il a déjà été marqué
                    double f = this.calculerFV2(object); //calcul de F
                    double pPrise = Math.pow(this.k_plus / (this.k_plus + f), 2); //calcul de la probabilité de prise
                    if (Math.random() <= pPrise) {
                        this.object = object;
                        this.environnement.prise(this); //2 agents sur l'objet, il peut etre porté
                        this.attendre = false; //les agents peuvent bouger
                        this.environnement.setMarquageAgent(this, false); //on enlève le marquage
                        this.signalRecu = false;
                        this.environnement.stopSignal(this, dSignal); //on arrete le signal
                    }
                } else { //si l'objet n'a pas déjà été marqué
                    this.emmettreSignal(); //on crée un signal
                    this.environnement.setMarquageAgent(this, true); //on marque la case
                    this.attendre = true; //l'agent attend de l'aide
                    this.signalEmis = true;
                }
            }
        }

    }

    public void emmettreSignal() { //creation du signal
        this.environnement.signal(this, this.dSignal);
    }

    public void depot() { //action depot
        String object = this.environnement.perceptionDepot(this); //appel à l'environnement
        if (object == "0") { //si la case ne contient pas d'objet
            double f = this.calculerFV2(this.object); //calcul de F
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

    public double calculerFV2(String object) { //calcul de F sans taux erreur
        int nbObj = 0;
        for (int i = 0; i < memoire.size(); i++) { //calcul de la fréquence de l'objet en paramètre dans la mémoire
            if (memoire.toArray()[i] == object) {
                nbObj++;
            }
        }
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

    public void ajouterMemoire(String objet) { //méthode d'ajout à la mémoire
        if (this.memoire.size() == this.tailleMemoire) {
            this.memoire.removeFirst(); //si la taille de la mémoire est maximale, on enlève la plus ancienne case parcourue de la mémoire
        }
        this.memoire.addLast(objet); //on ajoute la nouvelle case à la mémoire
    }

    public int getdSignal() {
        return dSignal;
    }

    public void setdSignal(int dSignal) {
        this.dSignal = dSignal;
    }

    public boolean isSignalRecu() {
        return signalRecu;
    }

    public void setSignalRecu(boolean signalRecu) {
        this.signalRecu = signalRecu;
    }

    public boolean isSignalEmis() {
        return signalEmis;
    }

    public void setSignalEmis(boolean signalEmis) {
        this.signalEmis = signalEmis;
    }

    public int[] getCoordSignal() {
        return coordSignal;
    }

    public void setCoordSignal(int[] coordSignal) {
        this.coordSignal = coordSignal;
    }



    public boolean isAttendre() {
        return attendre;
    }

    public void setAttendre(boolean attendre) {
        this.attendre = attendre;
    }



    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
