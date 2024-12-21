package utils.distance;

import domain.classify.Instance;

public class EuclideanDistance extends Distance {
    @Override
    public double computeDistance(Instance<Number, Integer> instance1, Instance<Number, Integer> instance2) {
        return Math.sqrt(
                Math.pow((Double.parseDouble(instance1.getInput().get(0).toString()) - Double.parseDouble(instance2.getInput().get(0).toString())), 2) +
                Math.pow((Double.parseDouble(instance1.getInput().get(1).toString()) - Double.parseDouble(instance2.getInput().get(1).toString())), 2) +
                Math.pow((Double.parseDouble(instance1.getInput().get(2).toString()) - Double.parseDouble(instance2.getInput().get(2).toString())), 2) +
                Math.pow((Double.parseDouble(instance1.getInput().get(3).toString()) - Double.parseDouble(instance2.getInput().get(3).toString())), 2) +
                Math.pow((Double.parseDouble(instance1.getInput().get(4).toString()) - Double.parseDouble(instance2.getInput().get(4).toString())), 2) +
                Math.pow((Double.parseDouble(instance1.getInput().get(5).toString()) - Double.parseDouble(instance2.getInput().get(5).toString())), 2) +
                Math.pow((Double.parseDouble(instance1.getInput().get(6).toString()) - Double.parseDouble(instance2.getInput().get(6).toString())), 2) +
                Math.pow((Double.parseDouble(instance1.getInput().get(7).toString()) - Double.parseDouble(instance2.getInput().get(7).toString())), 2)
        );
    }
}

