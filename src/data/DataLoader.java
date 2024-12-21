package data;

import domain.classify.Instance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DataLoader {
    protected String filename;
    protected List<Instance<Number, Integer>> listOfInstances;

    public DataLoader(String filename) throws RuntimeException {
        this.listOfInstances = new ArrayList<>();
        this.filename = filename;
        loadData();
    }

    public List<Instance<Number, Integer>> getListOfInstances() {
        return this.listOfInstances;
    }

    protected void loadData() throws RuntimeException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename)))
        {
            String line;
            int nrOfFeatures = br.readLine().split(",").length; // skip header

            while((line = br.readLine()) != null) {

                String[] tokens = line.split(",");
                if(tokens.length != nrOfFeatures){
                    throw new IOException("Dataset not valid!");
                }
                else{
                    try {
                        for (int i = 0; i < nrOfFeatures - 1; i++) {
                            Double.parseDouble(tokens[i]);
                        }

                        if (Integer.parseInt(tokens[8]) != 1 && Integer.parseInt(tokens[8]) != 0) {
                            throw new IOException("Dataset not valid!");
                        };
                    } catch (Exception e) {
                        throw new IOException("Dataset not valid!");
                    }
                    List<Number> paramList = new ArrayList<>();
                    for (int i = 0; i < nrOfFeatures - 1; i++) {
                        paramList.add(Double.parseDouble(tokens[i]));
                    }

                    Instance<Number, Integer> instance = new Instance<>(paramList, Integer.parseInt(tokens[8]));

                    listOfInstances.add(instance);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
