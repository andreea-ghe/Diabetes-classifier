package domain.evaluate;

import domain.classify.Instance;

import java.util.List;
import java.util.Objects;

public class F1Score implements EvaluationMeasure<Number, Integer> {

    @Override
    public double evaluate(List<Instance<Number, Integer>> instances, List<Integer> predictions) {
        int truePositives = 0;
        int falsePositives = 0;
        int falseNegatives = 0;
        for (int i = 0; i < instances.size(); i++) {
            if (predictions.get(i) == 1) {
                if (Objects.equals(instances.get(i).getOutput(), predictions.get(i))) {
                    truePositives++;
                } else {
                    falsePositives++;
                }
            } else {
                if (Objects.equals(instances.get(i).getOutput(), 1)) {
                    falseNegatives++;
                }
            }
        }

        double precision = (double) truePositives / (double) (truePositives + falsePositives);
        double recall =  (double) truePositives / (double) (truePositives + falseNegatives);

        return 2 * precision * recall / (precision + recall);
    }
}
