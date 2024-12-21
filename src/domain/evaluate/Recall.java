package domain.evaluate;

import domain.classify.Instance;

import java.util.List;
import java.util.Objects;

public class Recall implements EvaluationMeasure<Number, Integer> {

    @Override
    public double evaluate(List<Instance<Number, Integer>> instances, List<Integer> predictions) {
        int truePositives = 0;
        int falseNegative = 0;
        for (int i = 0; i < instances.size(); i++) {
            if (predictions.get(i) == 1) {
                if (Objects.equals(instances.get(i).getOutput(), predictions.get(i))) {
                    truePositives++;
                }
            } else {
                if (Objects.equals(instances.get(i).getOutput(), 1)) {
                    falseNegative++;
                }
            }
        }

        return (double) truePositives / (double) (truePositives + falseNegative);
    }
}

