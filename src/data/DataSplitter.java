package data;

import domain.classify.Instance;
import utils.fitInputs.FeatureScaler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataSplitter {
    protected Integer percentage;
    protected String filename;
    protected List<Instance<Number, Integer>> listOfAllInstances;
    protected List<Instance<Number, Integer>> listOfTrainingInstances = new ArrayList<>();
    protected List<Instance<Number, Integer>> listOfValidationInstances = new ArrayList<>();
    protected List<Instance<Number, Integer>> listOfTestingInstances = new ArrayList<>();
    private FeatureScaler scaler = new FeatureScaler();

    public DataSplitter(Integer percentage, String filename) {
        this.percentage = percentage;
        this.filename = filename;
        setListOfAllInstances();
        splitInstancesInGroups();
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

    public List<Instance<Number, Integer>> getListOfTrainingInstances() {
        return listOfTrainingInstances;
    }

    public List<Instance<Number, Integer>> getListOfValidationInstances() {
        return listOfValidationInstances;
    }

    public List<Instance<Number, Integer>> getListOfTestingInstances() {
        return listOfTestingInstances;
    }

    public void setListOfAllInstances() {
        DataLoader loader = new DataLoader(this.filename);
        this.listOfAllInstances = loader.getListOfInstances();
        this.scaler.fit(this.listOfAllInstances);
        this.scaler.transform(this.listOfAllInstances);
    }

    private void moveInstances(int numberOfInstances, List<Instance<Number, Integer>> listOfSourceInstances, List<Instance<Number, Integer>> listOfTargetInstances) {
        Random rand = new Random();

        while (numberOfInstances >= 1) {
            int randomNr = rand.nextInt(listOfSourceInstances.size());
            Instance<Number, Integer> instance = listOfSourceInstances.get(randomNr);
            listOfTargetInstances.add(instance);
            listOfSourceInstances.remove(instance);
            numberOfInstances -= 1;
        }
    }

    public void splitInstancesInGroups() {
        int numberOfTrainingIst = (int) ((double) percentage / 100 * listOfAllInstances.size());
        moveInstances(numberOfTrainingIst, listOfAllInstances, listOfTrainingInstances);
        moveInstances(listOfAllInstances.size(), listOfAllInstances, listOfTestingInstances);
        int numberOfValidationIst = (int) (0.1 * listOfTrainingInstances.size());
        moveInstances(numberOfValidationIst, listOfTrainingInstances, listOfValidationInstances);
    }

}
