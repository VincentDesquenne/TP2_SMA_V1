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
    private Agent agentAide;
    private Direction prochaineDirection;
    private int compteurSignal = 0;

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
        this.coordSignal = new int[2];
        this.prochaineDirection = null;
    }

    public void action() { //action de l'agent
        Random rand = new Random();
        for (int i = 0; i < pas; i++) {
            if (!attendre) {
                if (!this.signalRecu) {
                    ArrayList<Direction> directions = this.environnement.perceptionSeDeplacer(this); //appel à l'environnement
                    int dir = rand.nextInt(directions.size());
                    this.deplacer(directions.get(dir)); //déplacement
                    if (environnement.getAgentsObjetC().containsKey(this)) {
                        environnement.getAgentsObjetC().get(this).setProchaineDirection(directions.get(dir));
                    }
                } else {
                    this.deplacerVersSignal();
                }
            }
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

    public void deplacerVersSignal() {
        this.environnement.cheminVersSignal(this, coordSignal);
    }

    public void prise() { //action prise
        String object = this.environnement.perceptionPrise(this); //appel à l'environnement
        if (object != "0") { //si la case contient un objet et n'est pas un objet C
            if (object != "C") {
                double f = this.calculerFV2(object); //calcul de F
                double pPrise = Math.pow(this.k_plus / (this.k_plus + f), 2); //calcul de la probabilité de prise
                if (Math.random() <= pPrise) {
                    this.object = object;
                    this.environnement.prise(this); //action de prise de l'objet
                }
            } else {
                if (this.environnement.getMarquageAgent(this) && this.environnement.getAgentsObjetC().get(this) != null) {
                    this.object = object;
                    this.environnement.prise(this);
                    this.attendre = false;
                    this.environnement.getAgentsObjetC().get(this).setAttendre(false);
                    this.environnement.setMarquageAgent(this, false);
                    this.signalRecu = false;
                    this.compteurSignal = 0;
                } else {
                    //if(!signalEmis && compteurSignal < 10){
                        this.coordSignal[0] = this.environnement.perceptionPos(this)[0];
                        this.coordSignal[1] = this.environnement.perceptionPos(this)[1];
                        this.signalEmis = this.emmettreSignal();
                        this.environnement.setMarquageAgent(this, signalEmis);
                        this.attendre = true;
                        compteurSignal++;
                    /*} else {
                        this.environnement.setMarquageAgent(this, false);
                        this.attendre = false;
                        this.compteurSignal = 0;
                    }*/
                }
            }
        }
        if(this.object == "C")System.out.println("objet : " + this.object);

    }

    public boolean emmettreSignal() {
        return this.environnement.signal(this, this.dSignal);
    }

    public void depot() { //action depot
        String object = this.environnement.perceptionDepot(this); //appel à l'environnement
        if (object == "0") { //si la case ne contient pas d'objet
            double f = this.calculerFV2(this.object); //calcul de F
            double pDepot = Math.pow(f / (this.k_moins + f), 2); //calcul de la probabilité de dépot
            if (Math.random() <= pDepot) {
                this.environnement.depot(this, this.object); //action de dépot
                if (this.object == "C" && this.environnement.getAgentsObjetC().get(this) != null) {
                    this.environnement.getAgentsObjetC().get(this).setProchaineDirection(null);
                    this.setProchaineDirection(null);
                    this.environnement.getAgentsObjetC().remove(this.environnement.getAgentsObjetC().get(this));
                    this.environnement.getAgentsObjetC().remove(this);
                    System.out.println("depôt objet : " + this.object);

                }
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

    public double calculerFV2(String object) { //calcul de F
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

    public Agent getAgentAide() {
        return agentAide;
    }

    public void setAgentAide(Agent agentAide) {
        this.agentAide = agentAide;
    }

    public boolean isAttendre() {
        return attendre;
    }

    public void setAttendre(boolean attendre) {
        this.attendre = attendre;
    }

    public Direction getProchaineDirection() {
        return prochaineDirection;
    }

    public void setProchaineDirection(Direction prochaineDirection) {
        this.prochaineDirection = prochaineDirection;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
