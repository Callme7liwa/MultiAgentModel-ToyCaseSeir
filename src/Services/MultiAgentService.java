package Services;

import Models.Individual;

public interface MultiAgentService {


    public void initialize_individuals();
    public void initializeGrid();
    public void deplacer(Individual individual);
    public void infecterVoisinage(Individual individual);
    /*public void updateIndividuals();*/
    public void simulateIteration(int simulation,int iteration,String fileName);
    public void start();
}
