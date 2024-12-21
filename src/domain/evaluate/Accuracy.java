package domain.evaluate;

import domain.classify.Instance;

import java.util.List;
import java.util.Objects;

public class Accuracy implements EvaluationMeasure<Number, Integer> {

    @Override
    public double evaluate(List<Instance<Number, Integer>> instances, List<Integer> predictions) {
        int correctPredictions = 0;
        for (int i = 0; i < instances.size(); i++) {
            if (Objects.equals(instances.get(i).getOutput(), predictions.get(i))) {
                correctPredictions ++;
            }
        }

        return (double) correctPredictions / (double) instances.size();
    }
}
