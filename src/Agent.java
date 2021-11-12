import java.util.Random;

public class Agent {
    private double k_plus;
    private double k_moins;
    private int pas;
    private Environnement environnement;
    private String object;

    public Agent(double k_plus, double k_moins, int pas, Environnement environnement){
        this.k_plus = k_plus;
        this.k_moins = k_moins;
        this.pas = pas;
        this.environnement = environnement;
    }

    public void action(){
        Random rand = new Random();
        int intDirection = rand.nextInt(8);
        Direction direction = intToDirection(intDirection);
        int[] coord = this.environnement.perception(this, direction);
        this.environnement.deplacement(this, coord, direction);
    }

    public Direction intToDirection(int direction){
        switch(direction){
            case 0:
                return Direction.UPPER_LEFT;
            case 1:
                return Direction.UPPER;
            case 2:
                return Direction.UPPER_RIGHT;
            case 3:
                return Direction.RIGHT;
            case 4:
                return Direction.LOWER_RIGHT;
            case 5:
                return Direction.LOWER;
            case 6:
                return Direction.LOWER_LEFT;
            case 7:
                return Direction.LEFT;
        }
        return Direction.NONE;
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
}
