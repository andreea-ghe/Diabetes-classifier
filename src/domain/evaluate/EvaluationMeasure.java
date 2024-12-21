package domain.evaluate;

import domain.classify.Instance;

import java.util.List;

public interface EvaluationMeasure<F, L> {
    public abstract double evaluate(List<Instance<F, L>> instances, List<L> predictions);
}
