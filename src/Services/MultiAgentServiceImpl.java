package Services;

import Models.Individual;
import Models.Status;
import Utils.Coordonnees;
import Utils.CustomRandom;
import Utils.UtilsAttributs;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MultiAgentServiceImpl implements MultiAgentService {

    private  List<Individual> individuals;
    private  char[][] grid;
    private  CustomRandom customRandom;
    private  static MultiAgentServiceImpl  agentServiceImpl;

    public MultiAgentServiceImpl(){
        this.individuals = new ArrayList<>();;
        this.grid = new char[UtilsAttributs.GRID_SIZE][UtilsAttributs.GRID_SIZE];
        this.customRandom = CustomRandom.getInstance();
    }

    public static MultiAgentServiceImpl getInstance(){
        if(agentServiceImpl == null)
            agentServiceImpl = new MultiAgentServiceImpl();
        return agentServiceImpl;
    }

    // Initialiser les individus
    public void initialize_individuals() {
        for (int i = 0; i < UtilsAttributs.TOTAL_INDIVIDUALS; i++) {
            int x = customRandom.generateInt(UtilsAttributs.GRID_SIZE);
            int y = customRandom.generateInt(UtilsAttributs.GRID_SIZE);
            Coordonnees coordonnees = new Coordonnees(x,y);
            Status status = (i < UtilsAttributs.INITIAL_INFECTED) ? Status.I : Status.S;
            int dI = (int) customRandom.negExp(UtilsAttributs.INFECTED_DURATION);
            int dE = (int) customRandom.negExp(UtilsAttributs.EXPOSED_DURATION);
            int dR = (int) customRandom.negExp(UtilsAttributs.RECOVERED_DURATION);
            individuals.add(new Individual(coordonnees, status, dI, dE, dR));
        }
    }

    // Méthode pour initialiser la grille
    public  void initialize_grid() {
        // Initialisation de la grille avec l'état initial "S" (sain) pour toutes les cellules
        for (int i = 0; i < UtilsAttributs.GRID_SIZE; i++) {
            for (int j = 0; j < UtilsAttributs.GRID_SIZE; j++) {
                grid[i][j] = 'S';
            }
        }
        // Placement des individus dans la grille selon leur état initial
        for (Individual individual : individuals) {
            int x = individual.getCoordonnees().getX();
            int y = individual.getCoordonnees().getY();
            char status =   (individual.getStatus() == Status.I) ? 'I' : 'S';
            grid[x][y] = status;
        }
    }

    // Méthode pour gérer le déplacement aléatoire
    public  void deplacer(Individual individual) {
        Coordonnees coordinates ;
        boolean positionOccupied;
        System.out.println("Entre");
        do {
            int newX = (individual.getCoordonnees().getX() + customRandom.generateInt(3) - 1 + 300) % 300; // Déplacement aléatoire dans la grille toroïdale
            int newY = (individual.getCoordonnees().getY() + customRandom.generateInt(3) - 1 + 300) % 300;
            coordinates = new Coordonnees(newX, newY);
            positionOccupied = individuals.stream().anyMatch(i -> i.getCoordonnees().getX() == newX && i.getCoordonnees().getY() == newY);
        }while (positionOccupied || (coordinates.getX() == individual.getCoordonnees().getX() && coordinates.getY() == individual.getCoordonnees().getY())); // Assurez-vous que l'individu se déplace réellement
        System.out.println("Sorties");
        grid[individual.getCoordonnees().getX()][individual.getCoordonnees().getY()] = 'S'; // Videz l'ancienne case
        individual.setCoordonnees(coordinates);
        char status =   (individual.getStatus() == Status.I) ? 'I' :
                        (individual.getStatus() == Status.E) ? 'E' :
                        (individual.getStatus() == Status.R) ? 'R' : 'S';
        grid[individual.getCoordonnees().getX()][individual.getCoordonnees().getY()] = status; // Définir la nouvelle position
    }

    // Méthode pour gérer l'infection du voisinage
    public  void infecter_voisinage(Individual individual) {
        if (individual.getStatus() == Status.S) {
            int voisinsInfectieux = 0;
            // Vérifier le voisinage de Moore
            for (int i = individual.getCoordonnees().getX() - 1; i <= individual.getCoordonnees().getX() + 1; i++) {
                for (int j = individual.getCoordonnees().getY() - 1; j <= individual.getCoordonnees().getY() + 1; j++) {
                    int ni = (i + 300) % 300; // Gérer les bords toroïdaux
                    int nj = (j + 300) % 300;
                    if (grid[ni][nj] == 'I') {
                        voisinsInfectieux++;
                    }
                }
            }
            double probability = 1 - Math.exp(-0.5 * voisinsInfectieux);
            if ( customRandom.generateRandomDoubleValue() < probability) {
                individual.setStatus(Status.E);
                individual.setTimeInStatus(0);
            }
        }
    }

    // Méthode pour mettre à jour l'état de chaque individu
    /*public void updateIndividuals() {
        for (Individual individual : individuals) {
            if (individual.getStatus() == Status.E) {
                if (individual.getDe() > 0) {
                    individual.setDe(individual.getDe() - 1);
                    if (individual.getDe() == 0) {
                        individual.setStatus(Status.I);
                    }
                }
            } else if (individual.getStatus() == Status.I) {
                if (individual.getDI() > 0) {
                    individual.setDI(individual.getDI() - 1);
                    if (individual.getDI() == 0) {
                        individual.setStatus(Status.R);
                    }
                }
            } else if (individual.getStatus() == Status.R) {
                if (individual.getDr() > 0) {
                    individual.setDr(individual.getDr() - 1);
                    if (individual.getDr() == 0) {
                        individual.setStatus(Status.S);
                    }
                }
            }
        }
    }*/

    // Méthode pour exécuter une itération de la simulation
    public void simulate_iteration(int simulation,int iteration,String fileName) {
        // Déplacer tous les individus
        for (Individual individual : individuals) {
            deplacer(individual);
        }

        // Infecter le voisinage de chaque individu
        for (Individual individual : individuals) {
            infecter_voisinage(individual);
        }

        // Mettre à jour l'état de chaque individu
        for (Individual individual : individuals) {
            individual.evoluer();
        }
        // Enregistrer les statistiques
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            int totalExposed = 0;
            int totalInfected = 0;
            int totalRecovered = 0;
            int totalSuceptible = 0 ;
            for (Individual individual : individuals) {
                if (individual.getStatus() == Status.E) {
                    totalExposed++;
                } else if (individual.getStatus() == Status.I) {
                    totalInfected++;
                } else if (individual.getStatus() == Status.R) {
                    totalRecovered++;
                }else totalSuceptible++;
            }
            writer.write(iteration + "," + totalSuceptible + "," + totalExposed + "," + totalInfected + "," + totalRecovered);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void simulate(int simulation){
        String fileName = UtilsAttributs.OUTPUT_FILE_PREFIX+simulation+".csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("iteration,susceptible,exposed,infected,recovered");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int iteration=1 ; iteration< UtilsAttributs.TOTAL_ITERATIONS ; iteration++){
            simulate_iteration(simulation , iteration,fileName);
        }
    }

    public void start() {
        initialize_individuals();
        initialize_grid();
        for(int simulation=1 ; simulation <= 1 ; simulation++)
        {
            simulate(simulation);
        }
    }
}