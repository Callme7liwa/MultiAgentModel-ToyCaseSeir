package Services;

import Models.Individual;

public interface MultiAgentService {
    public void initialize_individuals();
    public void initialize_grid();
    public void deplacer(Individual individual);
    public void infecter_voisinage(Individual individual);
    /*public void updateIndividuals();*/
    public void simulate_iteration(int simulation,int iteration,String fileName);
    public void start();
}
