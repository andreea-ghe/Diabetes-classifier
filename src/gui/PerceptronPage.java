package gui;

import data.DataSplitter;
import domain.classify.classifiersModels.PerceptronClassifier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.Objects;

public class PerceptronPage extends ClassifierPage{

    public void train(ActionEvent event) {

        int percentage = 80;
        if (trainSlider.getValue() != 0) {
            percentage = (int) trainSlider.getValue();
        }

        if (!Objects.equals(filenameLabel.getText(), "filename")) {
            splitter = new DataSplitter(percentage, filenameLabel.getText());

            double learningRate = 0;
            if (!learningRateTextField.getText().isEmpty()) {
                learningRate = Double.parseDouble(learningRateTextField.getText());
            }

            int epochs = 0;
            if (!epochsTextField.getText().isEmpty()) {
                epochs = Integer.parseInt(epochsTextField.getText());
            }

            perceptronClassifier = new PerceptronClassifier(learningRate, epochs);
            perceptronClassifier.train(splitter.getListOfTrainingInstances());
            perceptronClassifier.validate(splitter.getListOfValidationInstances());
            testButton.setDisable(false);
        }

    }

    public void defaultLearning(ActionEvent event) {
        learningRateTextField.setText(String.valueOf(0.001));
    }

    public void test(ActionEvent event) {
        perceptronClassifier.test(splitter.getListOfTestingInstances());
        predictions = perceptronClassifier.getTestPredictions();

        setMetrics(splitter, predictions);
    }

    @FXML
    private TextField learningRateTextField;

    @FXML
    private Button defaultLearningRateButton;

    @FXML
    private TextField epochsTextField;
}
