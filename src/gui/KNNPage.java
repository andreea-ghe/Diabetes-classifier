package gui;

import data.DataSplitter;
import domain.classify.classifiersModels.KNNClassifier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import utils.distance.Distance;

import java.util.Objects;

public class KNNPage extends ClassifierPage {

    @Override
    public void train(ActionEvent event) {

        int percentage = 80;
        if (trainSlider.getValue() != 0) {
            percentage = (int) trainSlider.getValue();
        }

        if (!Objects.equals(filenameLabel.getText(), "filename")) {
            splitter = new DataSplitter(percentage, filenameLabel.getText());

            int k = 0;
            if (!kTextField.getText().isEmpty()) {
                k = Integer.parseInt(kTextField.getText());
            }

            Distance.DistanceType distance = null;
            if(distanceToggle.hasProperties()) {
                distance = Distance.DistanceType.valueOf(distanceToggle.selectedToggleProperty().getName());
            }

            knnClassifier = new KNNClassifier(k, distance);
            knnClassifier.train(splitter.getListOfTrainingInstances());
            knnClassifier.validate(splitter.getListOfValidationInstances());
            testButton.setDisable(false);
        }
    }

    public void defaultNeighbors(ActionEvent event) {
        kTextField.setText(String.valueOf((int) Math.sqrt((double) trainSlider.getValue() / 100 * 768)));
    }

    @Override
    public void test(ActionEvent event) {
        knnClassifier.test(splitter.getListOfTestingInstances());
        predictions = knnClassifier.getTestPredictions();

        setMetrics(splitter, predictions);
    }

    @FXML
    private TextField kTextField;

    @FXML
    private Button defaultKButton;

    @FXML
    private ToggleGroup distanceToggle;

    @FXML
    private RadioButton euclideanRadio;

    @FXML
    private RadioButton manhattanRadio;
}
