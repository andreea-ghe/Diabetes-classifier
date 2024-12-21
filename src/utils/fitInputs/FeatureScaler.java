package utils.fitInputs;

import domain.classify.Instance;

import java.util.List;

public class FeatureScaler {
    private double[] means;
    private double[] stds;

    public void fit(List<Instance<Number, Integer>> instances) {
        int featureCount = instances.getFirst().getInput().size();
        means = new double[featureCount];
        stds = new double[featureCount];

        // Calculate means
        for (Instance<Number, Integer> inst : instances) {
            List<Number> inputs = inst.getInput();
            for (int i = 0; i < featureCount; i++) {
                means[i] += inputs.get(i).doubleValue();
            }
        }

        for (int i = 0; i < featureCount; i++) {
            means[i] /= instances.size();
        }

        // Calculate standard deviations
        for (Instance<Number, Integer> inst : instances) {
            List<Number> inputs = inst.getInput();
            for (int i = 0; i < featureCount; i++) {
                double diff = inputs.get(i).doubleValue() - means[i];
                stds[i] += diff * diff;
            }
        }

        for (int i = 0; i < featureCount; i++) {
            stds[i] = Math.sqrt(stds[i] / instances.size());
            // If std is 0 (all values are identical), set it to 1 to avoid division by zero
            if (stds[i] == 0.0) {
                stds[i] = 1.0;
            }
        }
    }

    public void transform(List<Instance<Number, Integer>> instances) {
        for (Instance<Number, Integer> inst : instances) {
            List<Number> inputs = inst.getInput();
            for (int i = 0; i < inputs.size(); i++) {
                double standardizedValue = (inputs.get(i).doubleValue() - means[i]) / stds[i];
                // Replace the old value with the standardized value
                inputs.set(i, standardizedValue);
            }
        }
    }
}
