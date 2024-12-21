package domain.classify.classifiersModels;

import domain.classify.Instance;
import domain.classify.Model;
import domain.evaluate.Accuracy;
import utils.distance.Distance;
import utils.distance.EuclideanDistance;
import utils.distance.ManhattanDistance;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class KNNClassifier implements Model<Number, Integer> {

    protected Integer k = 0;
    protected Distance distance;
    protected List<Instance<Number, Integer>> trainingInstances;
    protected List<Integer> testPredictions;

    public KNNClassifier() {}

    public KNNClassifier(int k, Distance.DistanceType distance) {
        this.k = k;

        if (distance == Distance.DistanceType.EUCLIDEAN) {
            this.distance = new EuclideanDistance();
        } else if (distance == Distance.DistanceType.MANHATTAN) {
            this.distance = new ManhattanDistance();
        }
    }

    public List<Integer> getTestPredictions() {
        return testPredictions;
    }

    @Override
    public void train(List<Instance<Number, Integer>> instances) {
        this.trainingInstances = instances;
    }

    public void generalTest(List<Instance<Number, Integer>> instances, List<Integer> predictions) {

        for (Instance<Number, Integer> instance : instances) {

            PriorityQueue<Instance<Number, Integer>> nearestNeighbours = new PriorityQueue<>((i1, i2) ->
                    Double.compare(distance.computeDistance(instance, i2), distance.computeDistance(instance, i1))
            );

            for (Instance<Number, Integer> trainingInstance : this.trainingInstances) {
                double currentDistance = distance.computeDistance(instance, trainingInstance);

                if (nearestNeighbours.size() < k) {
                    nearestNeighbours.add(trainingInstance);
                } else {
                    Instance<Number, Integer> farthestNeighbor = nearestNeighbours.peek();
                    double farthestDistance = distance.computeDistance(instance, farthestNeighbor);
                    if (currentDistance < farthestDistance) {
                        nearestNeighbours.poll(); // remove the lowest priority instance, farthest neighbor
                        nearestNeighbours.add(trainingInstance);
                    }
                }
            }

            int truePredictions = 0;
            int falsePredictions = 0;
            for (Instance<Number, Integer> neighbour : nearestNeighbours) {
                if (neighbour.getOutput() == 1) {
                    truePredictions++;
                } else {
                    falsePredictions++;
                }
            }

            if (truePredictions > falsePredictions) {
                predictions.add(1);
            } else {
                predictions.add(0);
            }
        }
    }

    @Override
    public void validate(List<Instance<Number, Integer>> instances) {
        if (this.distance == null) {
            this.distance = new EuclideanDistance();
        }

        if (this.k == 0) {
            int bestK = (int) Math.sqrt(instances.size());
            double bestAccuracy = 0;
            Accuracy accuracyEvaluator = new Accuracy();

            for (int testK = Math.max(1, bestK - 5); testK <= bestK + 5; testK++) {
                this.k = testK;
                List<Integer> validPredictions = new ArrayList<>();
                generalTest(instances, validPredictions);

                double currentAccuracy = accuracyEvaluator.evaluate(instances, validPredictions);

                if (currentAccuracy > bestAccuracy) {
                    bestAccuracy = currentAccuracy;
                    bestK = testK;
                }
            }

            this.k = bestK;
        }
    }


    @Override
    public void test(List<Instance<Number, Integer>> instances) {
        testPredictions = new ArrayList<>();
        generalTest(instances, testPredictions);
    }

}