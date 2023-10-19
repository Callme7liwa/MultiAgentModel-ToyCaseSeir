package Models;

import java.util.ArrayList;
import java.util.List;

public class Cellule {
    int x ;
    int y ;
    List<Individual> individuals;

    public Cellule(int x, int y) {
        this.x = x;
        this.y = y;
        this.individuals = new ArrayList<>();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public List<Individual> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(List<Individual> individuals) {
        this.individuals = individuals;
    }

    public void addIndividual(Individual individual){
        this.individuals.add(individual);
    }
}
