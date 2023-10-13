package Services;

import Models.Individual;

public interface MultiAgentService {
    static final int GRID_SIZE = 300;
    static final int TOTAL_INDIVIDUALS = 20000;
    static final int INITIAL_INFECTED = 20;
    static final int TOTAL_ITERATIONS = 730;
    static final int NEIGHBORHOOD_SIZE = 8;
    static final double INFECTION_PROBABILITY = 0.5;
    static final int EXPOSED_DURATION = 3;
    static final int INFECTED_DURATION = 4;
    static final int RECOVERED_DURATION = 365;

    public void initialize_individuals();
    public void initializeGrid();
    public void deplacer(Individual individual);
    public void infecterVoisinage(Individual individual);
    public void updateIndividuals();
    public void simulateIteration(int iteration);
    public void start();
}
