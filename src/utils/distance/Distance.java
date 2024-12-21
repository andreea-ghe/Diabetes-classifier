package utils.distance;

import domain.classify.Instance;

public abstract class Distance {
    public enum DistanceType {
        EUCLIDEAN,
        MANHATTAN
    }

    public abstract double computeDistance(Instance<Number, Integer> instance1, Instance<Number, Integer> instance2);
}
