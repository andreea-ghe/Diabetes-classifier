package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.AnchorPane;

public class AppCtrl {
    public void initialize() {
        Pagination pagination = new Pagination(5, 0);
        pagination.setPageFactory(this::loadPageContent);

        rootPane.getChildren().add(pagination);
    }

    private Node loadPageContent(Integer pageIndex) {
        try {
            FXMLLoader loader;

            switch (pageIndex) {
                case 0:
                    loader = new FXMLLoader(getClass().getResource("KNNPage.fxml"));
                    Node knnPage = loader.load();

                    KNNPage knnController = loader.getController();
                    knnController.setAppCtrl(this);

                    return knnPage;
                case 1:
                    loader = new FXMLLoader(getClass().getResource("NaiveBayesPage.fxml"));
                    Node naiveBayesPage = loader.load();

                    NaiveBayesPage naiveBayesController = loader.getController();
                    naiveBayesController.setAppCtrl(this);

                    return naiveBayesPage;
                case 2:
                    loader = new FXMLLoader(getClass().getResource("PerceptronPage.fxml"));
                    Node perceptronPage = loader.load();

                    PerceptronPage perceptronController = loader.getController();
                    perceptronController.setAppCtrl(this);

                    return perceptronPage;
                case 3:
                    loader = new FXMLLoader(getClass().getResource("LogisticRegressionPage.fxml"));
                    Node logisticRegressionPage = loader.load();

                    LogisticRegressionPage logisticRegressionController = loader.getController();
                    logisticRegressionController.setAppCtrl(this);

                    return logisticRegressionPage;
                case 4:
                    loader = new FXMLLoader(getClass().getResource("DecisionTreePage.fxml"));
                    Node decisionTreePage = loader.load();

                    DecisionTreePage decisionTreeController = loader.getController();
                    decisionTreeController.setAppCtrl(this);

                    return decisionTreePage;
                default:
                    return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @FXML
    private AnchorPane rootPane;

    @FXML
    protected Label accuracyLabel;

    @FXML
    protected Label precisionLabel;

    @FXML
    protected Label recallLabel;

    @FXML
    protected Label f1Label;

    @FXML
    protected Label truePositivesLabel;

    @FXML
    protected Label falsePositivesLabel;

    @FXML
    protected Label falseNegativesLabel;

    @FXML
    protected Label trueNegativesLabel;
}
