package gui;

import data.DataSplitter;
import domain.classify.classifiersModels.NaiveBayesClassifier;
import javafx.event.ActionEvent;

import java.util.Objects;

public class NaiveBayesPage extends ClassifierPage{

    @Override
    public void train(ActionEvent event) {
        int percentage = 80;
        if (trainSlider.getValue() != 0) {
            percentage = (int) trainSlider.getValue();
        }

        if (!Objects.equals(filenameLabel.getText(), "filename")) {
            splitter = new DataSplitter(percentage, filenameLabel.getText());

            naiveBayesClassifier = new NaiveBayesClassifier();
            naiveBayesClassifier.train(splitter.getListOfTrainingInstances());
            naiveBayesClassifier.validate(splitter.getListOfValidationInstances());
            testButton.setDisable(false);
        }
    }

    @Override
    public void test(ActionEvent event) {
        naiveBayesClassifier.test(splitter.getListOfTestingInstances());
        predictions = naiveBayesClassifier.getTestPredictions();

        setMetrics(splitter, predictions);
    }
}
