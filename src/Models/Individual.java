package Models;

import Utils.Coordonnees;
import Utils.CustomRandom;
import Utils.UtilsAttributs;

public class Individual {
    Coordonnees coordonnees;
    Status status;
    int timeInStatus;
    int dE;
    int dI;
    int dR;

    public Individual(Coordonnees coordonnees, Status status, int dI, int dE, int dR) {
        this.coordonnees = coordonnees;
        this.status = status;
        this.timeInStatus = 0;
        this.dE = dE;
        this.dI = dI;
        this.dR = dR;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setCoordonnees(Coordonnees coordonnees){
        this.coordonnees.setX(coordonnees.getX());
        this.coordonnees.setY(coordonnees.getY());
    }

    public Coordonnees getCoordonnees()
    {
        return this.coordonnees;
    }

    public int getTimeInStatus() {
        return this.timeInStatus;
    }

    public void setTimeInStatus(int timeInStatus) {
        this.timeInStatus = timeInStatus;
    }

    public int getDe() {
        return this.dE;
    }

    public void setDe(int dE) {
        this.dE = dE;
    }

    public int getDI() {
        return this.dI;
    }

    public void setDI(int dI) {
        this.dI = dI;
    }

    public int getDr() {
        return this.dR;
    }

    public void setDr(int dR) {
        this.dR = dR;
    }

    public void evoluer() {
        timeInStatus++;
        if (status == Status.E && timeInStatus >= dE) {
            status = Status.I; // Passage de E à I
            timeInStatus = 0;
        } else if (status == Status.I && timeInStatus >= dI) {
            status = Status.R; // Passage de I à R
            timeInStatus = 0;
        } else if (status == Status.R && timeInStatus >= dR) {
            status = Status.S; // Passage de R à S
            timeInStatus = 0;
        }
    }

}

