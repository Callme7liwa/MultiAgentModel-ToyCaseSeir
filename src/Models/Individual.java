package Models;

import Utils.CustomRandom;
import Utils.UtilsAttributs;

import java.util.function.Function;

public class Individual {
    int x;
    int y;
    Status status;
    int timeInStatus;
    int de;
    int dl;
    int dr;

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getTimeInStatus() {
        return this.timeInStatus;
    }

    public void setTimeInStatus(int timeInStatus) {
        this.timeInStatus = timeInStatus;
    }

    public int getDe() {
        return this.de;
    }

    public void setDe(int de) {
        this.de = de;
    }

    public int getDl() {
        return this.dl;
    }

    public void setDl(int dl) {
        this.dl = dl;
    }

    public int getDr() {
        return this.dr;
    }

    public void setDr(int dr) {
        this.dr = dr;
    }

    public Individual(int x, int y, Status status) {
        CustomRandom random = CustomRandom.getInstance();
        this.x = x;
        this.y = y;
        this.status = status;
        this.timeInStatus = 0;
        this.de = (int) random.negExp(UtilsAttributs.EXPOSED_DURATION);
        this.dl = (int) random.negExp(UtilsAttributs.INFECTED_DURATION);
        this.dr = (int) random.negExp(UtilsAttributs.RECOVERED_DURATION);
    }

    public void evoluer() {
        timeInStatus++;
        if (status == Status.E && timeInStatus >= de) {
            status = Status.I; // Passage de E à I
            timeInStatus = 0;
        } else if (status == Status.I && timeInStatus >= dl) {
            status = Status.R; // Passage de I à R
            timeInStatus = 0;
        } else if (status == Status.R && timeInStatus >= dr) {
            status = Status.S; // Passage de R à S
            timeInStatus = 0;
        }
    }
}

