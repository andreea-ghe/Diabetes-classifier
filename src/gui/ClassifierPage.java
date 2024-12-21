package gui;

import data.DataSplitter;
import domain.classify.classifiersModels.*;
import domain.evaluate.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

public abstract class ClassifierPage {
    AppCtrl appCtrl;
    DataSplitter splitter;
    KNNClassifier knnClassifier;
    NaiveBayesClassifier naiveBayesClassifier;
    PerceptronClassifier perceptronClassifier;
    LogisticRegressionClassifier logisticRegressionClassifier;
    DecisionTreeClassifier decisionTreeClassifier;
    List<Integer> predictions;

    public void setAppCtrl(AppCtrl appCtrl) {
        this.appCtrl = appCtrl;
    }

    public void initialize() {
        testButton.setDisable(true);

        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 80);
        trainSpinner.setValueFactory(valueFactory);

        trainSlider.setValue(80);
        trainSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            trainSpinner.getValueFactory().setValue(newVal.intValue());
        });

        trainSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            trainSlider.setValue(newVal);
        });
    }

    public void chooseFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            filenameLabel.setText(file.getAbsolutePath());
        }
    }

    public abstract void train(ActionEvent event);

    public abstract void test(ActionEvent event);

    public void setMetrics(DataSplitter splitter, List<Integer> predictions) {
        Accuracy acs = new Accuracy();
        appCtrl.accuracyLabel.setText(String.valueOf(acs.evaluate(splitter.getListOfTestingInstances(), predictions)));
        Precision pres = new Precision();
        appCtrl.precisionLabel.setText(String.valueOf(pres.evaluate(splitter.getListOfTestingInstances(), predictions)));
        Recall recs = new Recall();
        appCtrl.recallLabel.setText(String.valueOf(recs.evaluate(splitter.getListOfTestingInstances(), predictions)));
        F1Score fs = new F1Score();
        appCtrl.f1Label.setText(String.valueOf(fs.evaluate(splitter.getListOfTestingInstances(), predictions)));

        ConfusionMatrix cm = new ConfusionMatrix();
        int[][] confusionMatrix = cm.computeMatrix(splitter.getListOfTestingInstances(), predictions);
        appCtrl.truePositivesLabel.setText(String.valueOf(confusionMatrix[0][0]));
        appCtrl.falseNegativesLabel.setText(String.valueOf(confusionMatrix[0][1]));
        appCtrl.falsePositivesLabel.setText(String.valueOf(confusionMatrix[1][0]));
        appCtrl.trueNegativesLabel.setText(String.valueOf(confusionMatrix[1][1]));
    }

    @FXML
    protected Button chooseFile;

    @FXML
    protected Label filenameLabel;

    @FXML
    protected Button trainButton;

    @FXML
    protected Slider trainSlider;

    @FXML
    protected Spinner<Integer> trainSpinner;

    @FXML
    protected Button testButton;
}
