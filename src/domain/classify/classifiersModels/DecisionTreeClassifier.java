package domain.classify.classifiersModels;

import domain.classify.Instance;
import domain.classify.Model;

import java.util.*;
import java.util.stream.Collectors;

public class DecisionTreeClassifier implements Model<Number, Integer> {
    protected Node root;
    protected List<Integer> testPredictions;

    public List<Integer> getTestPredictions() {
        return testPredictions;
    }

    @Override
    public void train(List<Instance<Number, Integer>> instances) {
        this.root = buildTree(instances, new HashSet<>(), 0);
    }

    @Override
    public void validate(List<Instance<Number, Integer>> instances) {}

    @Override
    public void test(List<Instance<Number, Integer>> instances) {
        testPredictions = new ArrayList<>();
        for (Instance<Number, Integer> instance : instances) {
            Node current = root;
            Node next = null;
            while (!current.isLeaf()) {
                double value = instance.getInput().get(Integer.parseInt(current.attribute.split("_")[1])).doubleValue();
                next = value <= current.threshold ? current.left : current.right;
                if (next == null) {
                    break;
                }
                current = next;
            }
            testPredictions.add(current.label);
        }
    }

    private double computeEntropy(List<Instance<Number, Integer>> instances) {
        Map<Integer, Long> labelCounts = instances.stream()
                .map(Instance::getOutput)
                .collect(Collectors.groupingBy(label -> label, Collectors.counting()));

        double total = instances.size();
        return labelCounts.values().stream()
                .mapToDouble(count -> {
                    double p = count / total;
                    return -p * Math.log(p) / Math.log(2);
                }).sum();
    }

    private double[] computeThresholds(List<Instance<Number, Integer>> instances, int attributeColumn) {
        List<Double> values = new ArrayList<>();
        for (Instance<Number, Integer> instance : instances) {
            values.add(instance.getInput().get(attributeColumn).doubleValue());
        }

        return values.stream()
                .distinct()
                .sorted()
                .mapToDouble(d -> d)
                .toArray();
    }

    private double computeInformationGain(List<Instance<Number, Integer>> instances, int attributeColumn, double threshold, double baseEntropy) {
        List<Instance<Number, Integer>> leftSubset = new ArrayList<>();
        List<Instance<Number, Integer>> rightSubset = new ArrayList<>();

        for (Instance<Number, Integer> instance : instances) {
            if (instance.getInput().get(attributeColumn).doubleValue() <= threshold) {
                leftSubset.add(instance);
            } else {
                rightSubset.add(instance);
            }
        }

        return baseEntropy
                - ((double) leftSubset.size() / instances.size()) * computeEntropy(leftSubset)
                - ((double) rightSubset.size() / instances.size()) * computeEntropy(rightSubset);
    }

    private int majorityLabel(List<Instance<Number, Integer>> instances) {
        // count occurrences of each label
        Map<Integer, Integer> labelCounts = new HashMap<>();
        for (Instance<Number, Integer> instance : instances) {
            int label = instance.getOutput();
            labelCounts.put(label, labelCounts.getOrDefault(label, 0) + 1);
        }

        int majorityLabel = -1;
        int maxCount = 0;
        for (Map.Entry<Integer, Integer> entry : labelCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                majorityLabel = entry.getKey();
                maxCount = entry.getValue();
            }
        }

        return majorityLabel;
    }


    private boolean allSameLabel(List<Instance<Number, Integer>> instances) {
        // if all instances same label => leaf
        int firstLabel = instances.getFirst().getOutput();

        for (Instance<Number, Integer> instance : instances) {
            if (!instance.getOutput().equals(firstLabel)) {
                return false;
            }
        }

        return true;
    }

    private Node buildTree(List<Instance<Number, Integer>> instances, Set<Integer> usedFeatures, int depth) {
        if (instances.isEmpty())
            return null;

        if (allSameLabel(instances)) {
            Node leaf = new Node();
            leaf.label = instances.getFirst().getOutput();
            return leaf;
        }

        // compute IG for each attribute and find the best split
        double baseEntropy = computeEntropy(instances);
        double bestIG = -1;
        int bestAttribute = -1;
        double bestThreshold = 0;

        for (int attributeColumn = 0; attributeColumn < instances.getFirst().getInput().size(); attributeColumn++) {
            if (usedFeatures.contains(attributeColumn))
                continue;

            double[] thresholds = computeThresholds(instances, attributeColumn);
            for (double threshold : thresholds) {
                double ig = computeInformationGain(instances, attributeColumn, threshold, baseEntropy);
                if (ig > bestIG) {
                    bestIG = ig;
                    bestAttribute = attributeColumn;
                    bestThreshold = threshold;
                }
            }
        }

        if (bestAttribute == -1) {
            Node leaf = new Node();
            leaf.label = majorityLabel(instances);
            return leaf;
        }

        // split data and create subtrees
        List<Instance<Number, Integer>> leftSubset = new ArrayList<>();
        List<Instance<Number, Integer>> rightSubset = new ArrayList<>();

        for (Instance<Number, Integer> instance : instances) {
            if (instance.getInput().get(bestAttribute).doubleValue() <= bestThreshold) {
                leftSubset.add(instance);
            } else {
                rightSubset.add(instance);
            }
        }

        Node node = new Node();
        node.attribute = "Attribute_" + bestAttribute;
        node.threshold = bestThreshold;
        usedFeatures.add(bestAttribute);

        node.left = buildTree(leftSubset, new HashSet<>(usedFeatures), depth + 1);
        node.right = buildTree(rightSubset, new HashSet<>(usedFeatures), depth + 1);

        return node;

    }

    private static class Node {
        private String attribute;
        private double threshold;
        private int label;
        private Node left;
        private Node right;

        private boolean isLeaf() {
            return attribute == null;
        }
    }

}
