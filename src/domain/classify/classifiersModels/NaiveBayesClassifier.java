package domain.classify.classifiersModels;

import domain.classify.Instance;
import domain.classify.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NaiveBayesClassifier implements Model<Number, Integer> {

    private Map<Integer, Double> classPriors = new HashMap<>(); // P(C) probability true/false
    private Map<Integer, double[][]> meanAndVariance = new HashMap<>(); // Mean and Variance for each feature per class
    private List<Integer> testPredictions;


    public List<Integer> getTestPredictions() {
        return testPredictions;
    }

    private double gaussianProbability(double x, double mean, double variance) {
        return (1 / Math.sqrt(2 * Math.PI * variance)) * Math.exp(-((x - mean) * (x - mean)) / (2 * variance));
    }

    @Override
    public void train(List<Instance<Number, Integer>> instances) {
        Map<Integer, Integer> classCounts = new HashMap<>();
        for (Instance<Number, Integer> instance : instances) {
                classCounts.put(instance.getOutput(), classCounts.getOrDefault(instance.getOutput(), 0) + 1);
        }

        for (Map.Entry<Integer, Integer> entry : classCounts.entrySet()) {
            this.classPriors.put(entry.getKey(), (double) entry.getValue() / instances.size());
        }


        for (Integer classLabel : classCounts.keySet()) {
            List<Instance<Number, Integer>> classInstances = new ArrayList<>(); // instances for true or false
            for (Instance<Number, Integer> instance : instances) {
                if (instance.getOutput().equals(classLabel)) {
                    classInstances.add(instance);
                }
            }

            int numFeatures = classInstances.getFirst().getInput().size(); // there are 8 but is good for generalising
            double[][] mandv = new double[numFeatures][2]; // [mean, variance] for each feature

            for (int attribute = 0; attribute < numFeatures; attribute++) {

                // calculate mean
                double sum = 0;
                for (Instance<Number, Integer> instance : classInstances) {
                    sum += instance.getInput().get(attribute).doubleValue();
                }
                double mean = sum / classInstances.size();

                // calculate variance
                double varianceSum = 0;
                for (Instance<Number, Integer> instance : classInstances) {
                    double diff = instance.getInput().get(attribute).doubleValue() - mean;
                    varianceSum += diff * diff;
                }
                double variance = varianceSum / classInstances.size();

                mandv[attribute][0] = mean;
                mandv[attribute][1] = variance;
            }

            this.meanAndVariance.put(classLabel, mandv);
        }
    }

    @Override
    public void validate(List<Instance<Number, Integer>> instances) {}

    @Override
    public void test(List<Instance<Number, Integer>> instances) {
        this.testPredictions = new ArrayList<>();

        for (Instance<Number, Integer> instance : instances) {
            Map<Integer, Double> posteriorProbs = new HashMap<>();

            for (Integer classLabel : this.classPriors.keySet()) {
                double logPosterior = Math.log(classPriors.get(classLabel)); // Log to avoid precision issues
                double[][] mandv = this.meanAndVariance.get(classLabel);

                for (int attribute = 0; attribute < instance.getInput().size(); attribute++) {
                    double x = instance.getInput().get(attribute).doubleValue();
                    double mean = mandv[attribute][0];
                    double variance = mandv[attribute][1];

                    logPosterior += Math.log(gaussianProbability(x, mean, variance));
                }

                posteriorProbs.put(classLabel, logPosterior);
            }

            // choose class with the highest posterior probability
            int predictedClass = posteriorProbs.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .get()
                    .getKey();

            this.testPredictions.add(predictedClass);
        }
    }
}
