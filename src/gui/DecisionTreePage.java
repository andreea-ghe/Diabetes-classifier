package gui;

import data.DataSplitter;
import domain.classify.classifiersModels.DecisionTreeClassifier;
import javafx.event.ActionEvent;

import java.util.Objects;

public class DecisionTreePage extends ClassifierPage {

    @Override
    public void train(ActionEvent event) {
        int percentage = 80;
        if (trainSlider.getValue() != 0) {
            percentage = (int) trainSlider.getValue();
        }

        if (!Objects.equals(filenameLabel.getText(), "filename")) {
            splitter = new DataSplitter(percentage, filenameLabel.getText());

            decisionTreeClassifier = new DecisionTreeClassifier();
            decisionTreeClassifier.train(splitter.getListOfTrainingInstances());
            decisionTreeClassifier.validate(splitter.getListOfValidationInstances());
            testButton.setDisable(false);
        }
    }

    @Override
    public void test(ActionEvent event) {
        decisionTreeClassifier.test(splitter.getListOfTestingInstances());
        predictions = decisionTreeClassifier.getTestPredictions();

        setMetrics(splitter, predictions);
    }
}
