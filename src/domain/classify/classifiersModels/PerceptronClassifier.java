package domain.classify.classifiersModels;

import domain.classify.Instance;
import domain.classify.Model;
import domain.evaluate.Accuracy;

import java.util.*;
import java.util.stream.Stream;

public class PerceptronClassifier implements Model<Number, Integer> {
    protected double[] weights;
    protected double bias;
    protected int epochs = 0;
    protected double learningRate = 0;
    protected List<Integer> testPredictions;
    protected List<Instance<Number, Integer>> trainInstances;


    public PerceptronClassifier(double learningRate, int epochs) {
        this.learningRate = learningRate;
        this.epochs = epochs;
        initialize();
    }

    public PerceptronClassifier() {
        initialize();
    }

    public void initialize() {
        this.weights = new double[8];
        Arrays.fill(this.weights, 0);
        this.bias = 0;
    }

    public List<Integer> getTestPredictions() {
        return this.testPredictions;
    }

    private double linearCombination(List<Number> input) {
        double sum = this.bias;
        for (int i = 0; i < this.weights.length; i++) {
            sum += this.weights[i] * input.get(i).doubleValue();
        }                                                                                                                                     
        return sum;                                                                                                                           
    }                                                                                                                                         
                                                                                                                                              
    @Override                                                                                                                                 
    public void train(List<Instance<Number, Integer>> instances) {
        this.trainInstances = instances;                                                                                                      

        for (int e = 0; e < epochs; e ++) {
            Collections.shuffle(instances, new Random());
            for (Instance<Number, Integer> instance : instances) {
                double f = linearCombination(instance.getInput());
                int y = f >= 0? 1 : 0;

                if (y != instance.getOutput()) {
                    int loss = instance.getOutput() - y;
                    for (int i = 0; i < this.weights.length; i++) {
                        this.weights[i] += learningRate * loss * instance.getInput().get(i).doubleValue();
                    }
                    this.bias += learningRate * loss;
                } 
            }
        }
    }

    @Override
    public void validate(List<Instance<Number, Integer>> instances) {

        if (learningRate == 0 && epochs == 0) {

            double bestLearningRate = 0;
            int bestNrEpochs = 0;
            double bestAccuracy = 0;

            List<Double> learningRates = Stream.of(0.001, 0.01, 0.1).toList();

            List<Integer> nrEpochs = Stream.of( 100, 200, 300, 1000).toList();

            for (Integer epoch : nrEpochs) {
                this.epochs = epoch;

                for (Double learningR : learningRates) {
                    this.learningRate = learningR;
                    initialize();
                    train(this.trainInstances);

                    List<Integer> predictions = new ArrayList<>();
                    for (Instance<Number, Integer> instance : instances) {
                        double f = linearCombination(instance.getInput());
                        int y = f >= 0 ? 1 : 0;
                        predictions.add(y);
                    }

                    Accuracy accuracyEvaluator = new Accuracy();
                    double currentAccuracy = accuracyEvaluator.evaluate(instances, predictions);

                    if (currentAccuracy > bestAccuracy) {
                        bestAccuracy = currentAccuracy;
                        bestLearningRate = learningR;
                        bestNrEpochs = epoch;
                    }
                }
            }
            this.learningRate = bestLearningRate;
            this.epochs = bestNrEpochs;
            initialize();
            train(this.trainInstances);
        } else if (epochs == 0) {
            int bestNrEpochs = 0;
            double bestAccuracy = 0;

            List<Integer> nrEpochs = Stream.of( 100, 200, 300, 1000).toList();

            for (Integer epoch : nrEpochs) {
                this.epochs = epoch;

                initialize();
                train(this.trainInstances);

                List<Integer> predictions = new ArrayList<>();
                for (Instance<Number, Integer> instance : instances) {
                    double f = linearCombination(instance.getInput());
                    int y = f >= 0 ? 1 : 0;
                    predictions.add(y);
                }

                Accuracy accuracyEvaluator = new Accuracy();
                double currentAccuracy = accuracyEvaluator.evaluate(instances, predictions);

                if (currentAccuracy > bestAccuracy) {
                    bestAccuracy = currentAccuracy;
                    bestNrEpochs = epoch;
                }
            }

            this.epochs = bestNrEpochs;
            initialize();
            train(this.trainInstances);

        } else if (learningRate == 0) {
            double bestLearningRate = 0;
            double bestAccuracy = 0;

            List<Double> learningRates = Stream.of(0.001, 0.01, 0.1).toList();

            for (Double learningR : learningRates) {
                this.learningRate = learningR;
                initialize();
                train(this.trainInstances);

                List<Integer> predictions = new ArrayList<>();
                for (Instance<Number, Integer> instance : instances) {
                    double f = linearCombination(instance.getInput());
                    int y = f >= 0 ? 1 : 0;
                    predictions.add(y);
                }

                Accuracy accuracyEvaluator = new Accuracy();
                double currentAccuracy = accuracyEvaluator.evaluate(instances, predictions);

                if (currentAccuracy > bestAccuracy) {
                    bestAccuracy = currentAccuracy;
                    bestLearningRate = learningR;
                }
            }

            this.learningRate = bestLearningRate;
            initialize();
            train(this.trainInstances);
        }
    }

    @Override
    public void test(List<Instance<Number, Integer>> instances) {

        this.testPredictions = new ArrayList<>();
        for (Instance<Number, Integer> instance : instances) {
            double f = linearCombination(instance.getInput());
            int y = f >= 0 ? 1 : 0;
            this.testPredictions.add(y);
        }
    }
}