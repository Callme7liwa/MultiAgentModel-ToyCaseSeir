package Services;

import Models.Individual;
import Models.Status;
import Utils.CustomRandom;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MultiAgentServiceImpl implements MultiAgentService {

    private static final String OUTPUT_FILE = "results_2.csv";
    private static Random random = new Random(123); // 123 est la seed (remplacez par la valeur souhaitée)

    private  List<Individual> individuals;
    private  char[][] grid;
    private  CustomRandom customRandom;

    public MultiAgentServiceImpl(){
        this.individuals = new ArrayList<>();;
        this.grid = new char[GRID_SIZE][GRID_SIZE];
        this.customRandom = CustomRandom.getInstance();
    }

    // Initialiser les individus
    public void initialize_individuals() {
        for (int i = 0; i < TOTAL_INDIVIDUALS; i++) {
            int x = random.nextInt(GRID_SIZE);
            int y = random.nextInt(GRID_SIZE);
            Status status = (i < INITIAL_INFECTED) ? Status.I : Status.S;
            individuals.add(new Individual(x, y, status));
        }
    }

    // Méthode pour initialiser la grille
    public  void initializeGrid() {
        // Initialisation de la grille avec l'état initial "S" (sain) pour toutes les cellules
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = 'S';
            }
        }

        // Placement des individus dans la grille selon leur état initial
        for (Individual individual : individuals) {
            int x = individual.getX();
            int y = individual.getY();
            char status =   (individual.getStatus() == Status.I) ? 'I' :
                            (individual.getStatus() == Status.E) ? 'E' :
                            (individual.getStatus() == Status.R) ? 'R' : 'S';
            grid[x][y] = status;
        }
    }

    // Méthode pour gérer le déplacement aléatoire
    public  void deplacer(Individual individual) {
        int newX, newY;

        do {
            newX = (individual.getX() + random.nextInt(3) - 1 + 300) % 300; // Déplacement aléatoire dans la grille toroïdale
            newY = (individual.getY() + random.nextInt(3) - 1 + 300) % 300;
        } while (newX == individual.getX() && newY == individual.getY()); // Assurez-vous que l'individu se déplace réellement

        grid[individual.getX()][individual.getY()] = 'S'; // Videz l'ancienne case
        individual.setX(newX);
        individual.setY(newY);
        char status = (individual.getStatus() == Status.I) ? 'I' :
                (individual.getStatus() == Status.E) ? 'E' :
                        (individual.getStatus() == Status.R) ? 'R' : 'S';
        grid[individual.getX()][individual.getY()] = status; // Définir la nouvelle position
    }

    // Méthode pour gérer l'infection du voisinage
    public  void infecterVoisinage(Individual individual) {
        if (individual.getStatus() == Status.S) {
            int voisinsInfectieux = 0;
            // Vérifier le voisinage de Moore
            for (int i = individual.getX() - 1; i <= individual.getX() + 1; i++) {
                for (int j = individual.getY() - 1; j <= individual.getY() + 1; j++) {
                    int ni = (i + 300) % 300; // Gérer les bords toroïdaux
                    int nj = (j + 300) % 300;
                    if (grid[ni][nj] == 'I') {
                        voisinsInfectieux++;
                    }
                }
            }

            double probability = 1 - Math.exp(-0.5 * voisinsInfectieux);
            if (random.nextDouble() < probability) {
                individual.setStatus(Status.E);
            }
        }
    }

    // Méthode pour mettre à jour l'état de chaque individu
    public  void updateIndividuals() {
        for (Individual individual : individuals) {
            if (individual.getStatus() == Status.E) {
                individual.setDe(individual.getDe() - 1);
                if (individual.getDe() == 0) {
                    individual.setStatus(Status.I);
                }
            } else if (individual.getStatus() == Status.I) {
                individual.setDl(individual.getDl() - 1);
                if (individual.getDl() == 0) {
                    individual.setStatus(Status.R);
                }
            } else if (individual.getStatus() == Status.R) {
                individual.setDr(individual.getDr() - 1);
                if (individual.getDr() == 0) {
                    individual.setStatus(Status.S);
                }
            }
        }
    }

    // Méthode pour exécuter une itération de la simulation
    public void simulateIteration(int iteration) {
        // Déplacer tous les individus
        for (Individual individual : individuals) {
            deplacer(individual);
        }

        // Infecter le voisinage de chaque individu
        for (Individual individual : individuals) {
            infecterVoisinage(individual);
        }

        // Mettre à jour l'état de chaque individu
        updateIndividuals();

        // Enregistrer les statistiques
        try (PrintWriter writer = new PrintWriter(new FileWriter(OUTPUT_FILE, true))) {
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
            writer.println(iteration + "," + totalSuceptible + "," + totalExposed + "," + totalInfected + "," + totalRecovered);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        initialize_individuals();
        initializeGrid();

        try (PrintWriter writer = new PrintWriter(new FileWriter(OUTPUT_FILE))) {
            writer.println("iteration,susceptible,exposed,infected,recovered");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int iteration = 1; iteration <= TOTAL_ITERATIONS; iteration++) {
            simulateIteration(iteration);
        }
    }
}