package Models;

import Utils.Coordonnees;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Grille {
    int nombreColonne;
    int nombreLigne ;
    Cellule[][] cellules ;

    public Grille(int nombreColonne, int nombreLigne) {
        this.nombreColonne = nombreColonne;
        this.nombreLigne = nombreLigne;
        this.cellules = new Cellule[nombreColonne][nombreLigne];

        // Assign column and row numbers to each cell
        for (int x = 0; x < nombreColonne; x++) {
            for (int y = 0; y < nombreLigne; y++) {
                cellules[x][y] = new Cellule(x, y);
            }
        }
    }

    public int getNombreColonne() {
        return nombreColonne;
    }

    public void setNombreColonne(int nombreColonne) {
        this.nombreColonne = nombreColonne;
    }

    public int getNombreLigne() {
        return nombreLigne;
    }

    public void setNombreLigne(int nombreLigne) {
        this.nombreLigne = nombreLigne;
    }

    public Cellule[][] getCellules() {
        return cellules;
    }

    public void setCellules(Cellule[][] cellules) {
        this.cellules = cellules;
    }

    public Cellule getCelluleByCoordinates(Coordonnees coordonnees) {
        return this.cellules[coordonnees.getX()][coordonnees.getY()];
    }
}
