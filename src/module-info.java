module diabetes_classifier {
    requires javafx.fxml;
    requires org.xerial.sqlitejdbc;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.dataformat.xml;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires com.gluonhq.charm.glisten;
    requires com.gluonhq.attach.util;
    requires com.ctc.wstx;

    opens gui to javafx.fxml;
    exports gui;
    exports domain.classify;
    exports domain.classify.classifiersModels;
    exports domain.evaluate;
}