package com.swe2.http_requester;

import java.util.concurrent.ThreadLocalRandom;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;

/// Controls the programs main view
public class HttpRequesterController {
    @FXML
    private TableView<TableContent> table;

    @FXML
    public TableColumn<TableContent, String> name;

    @FXML
    public TableColumn<TableContent, String> value;

    @FXML
    private Label requestStatus;

    @FXML
    public void initialize() {
        generateEmptyTable();
    }

    /// Generates an empty table
    ///
    /// Doesn't contain the final logic. Will be changed (or deleted) at some point
    /// during the semester
    public void generateEmptyTable() {
        ObservableList<TableContent> items = table.getItems();

        for (int i = 0; i < 20; i++) {
            items.add(new TableContent("", ""));

        }
    }

    /// Sends an http request
    ///
    /// Doesn't contain the final logic. Will be changed (or deleted) at some point
    /// during the semester
    public void sendRequest() {
        if (ThreadLocalRandom.current().nextBoolean()) {
            requestStatus.setText("OK");
            requestStatus.setTextFill(Color.valueOf("green"));
        } else {
            requestStatus.setText("NOK");
            requestStatus.setTextFill(Color.valueOf("red"));
        }

    }
}
