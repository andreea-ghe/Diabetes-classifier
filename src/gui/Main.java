package gui;

import data.DataSplitter;
import domain.classify.classifiersModels.LogisticRegressionClassifier;
import domain.evaluate.Accuracy;
import domain.evaluate.F1Score;
import domain.evaluate.Precision;
import domain.evaluate.Recall;

public class Main {
    public static void main(String[] args) {
        DataSplitter splitter = new DataSplitter(99, "C:/Users/Andreea/Documents/MAP/classifier/src/data/diabetes.csv");

//        KNNClassifier knnClassifier = new KNNClassifier();
//        knnClassifier.train(splitter.getListOfTrainingInstances());
//        knnClassifier.validate(splitter.getListOfValidationInstances());
//        knnClassifier.test(splitter.getListOfTestingInstances());

        LogisticRegressionClassifier knnClassifier = new LogisticRegressionClassifier();
        knnClassifier.train(splitter.getListOfTrainingInstances());
        knnClassifier.validate(splitter.getListOfValidationInstances());
        knnClassifier.test(splitter.getListOfTestingInstances());

        Accuracy acs = new Accuracy();
        System.out.println(String.valueOf(acs.evaluate(splitter.getListOfTestingInstances(), knnClassifier.getTestPredictions())));
        Precision pres = new Precision();
        System.out.println(String.valueOf(pres.evaluate(splitter.getListOfTestingInstances(), knnClassifier.getTestPredictions())));
        Recall recs = new Recall();
        System.out.println(String.valueOf(recs.evaluate(splitter.getListOfTestingInstances(), knnClassifier.getTestPredictions())));
        F1Score fs = new F1Score();
        System.out.println(String.valueOf(fs.evaluate(splitter.getListOfTestingInstances(), knnClassifier.getTestPredictions())));

    }
}
