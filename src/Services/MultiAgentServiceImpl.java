package Services;

import Models.Cellule;
import Models.Grille;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MultiAgentServiceImpl implements MultiAgentService {

    private  List<Individual> individuals;
    private  Grille grid;
    private  CustomRandom customRandom;
    private  static MultiAgentServiceImpl  agentServiceImpl;
    List<Coordonnees> emptySpaces = new ArrayList<>();

    public MultiAgentServiceImpl(){
        this.individuals = new ArrayList<>();;
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
            individuals.add(new Individual(i,coordonnees, status, dI, dE, dR));
        }
    }

    // Méthode pour initialiser la grille
    public void initialize_grid() {
        this.grid = new Grille(UtilsAttributs.GRID_SIZE, UtilsAttributs.GRID_SIZE);

        // Placement des individus dans la grille selon leur état initial
        for (Individual individual : individuals) {
            int x = individual.getCoordonnees().getX();
            int y = individual.getCoordonnees().getY();
            Cellule cellule = null;

            // Find the cell with matching coordinates
            cellule = grid.getCelluleByCoordinates(new Coordonnees(x,y));

            // Add individual to the cell if found
            if (cellule != null) {
                cellule.addIndividual(individual);
            }

        }
    }

    // Méthode pour gérer le déplacement aléatoire
    public  void deplacer(Individual individual) {
        int newX;
        int newY;
        do {
            newX = (individual.getCoordonnees().getX() + customRandom.generateInt(3) - 1 + 300) % 300;
            newY = (individual.getCoordonnees().getY() + customRandom.generateInt(3) - 1 + 300) % 300;
        } while ((newX == individual.getCoordonnees().getX() && newY == individual.getCoordonnees().getY())); // Assurez-vous que l'individu se déplace réellement

        Cellule oldCellule = this.grid.getCelluleByCoordinates(individual.getCoordonnees());
        oldCellule.getIndividuals().remove(individual);
        Cellule newCellule=this.grid.getCellules()[newX][newY];
        newCellule.getIndividuals().add(individual);
        individual.setCoordonnees(new Coordonnees(newX,newY));
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
                    for(Individual individ: this.grid.getCellules()[ni][nj].getIndividuals())
                        if (individ.getStatus() == Status.I && individ.getId() != individual.getId())
                            voisinsInfectieux++;
                }
            }
            double probability = 1 - Math.exp(-0.5 * voisinsInfectieux);
            if ( customRandom.generateRandomDoubleValue() < probability) {
                individual.setStatus(Status.E);
                individual.setTimeInStatus(0);
            }
        }
    }

    // Méthode pour exécuter une itération de la simulation
    public void simulate_iteration(int simulation,int iteration,String fileName)
    {
        Collections.shuffle(individuals);

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
            System.out.println("iteration"+iteration);
            simulate_iteration(simulation , iteration,fileName);
        }
    }

    public void start() {
        initialize_individuals();
        initialize_grid();
        for(int simulation=1 ; simulation <= UtilsAttributs.NUMBER_SIMULATION; simulation++)
        {
            System.out.println("simulation"+simulation);
            simulate(simulation);
        }
    }
}