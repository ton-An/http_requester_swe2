package com.swe2.http_requester;

import java.util.concurrent.ThreadLocalRandom;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;

public class HttpRequesterController {
    @FXML
    private TableView<String> table;

    @FXML
    private Label requestStatus;

    @FXML
    public void initialize() {
        generateEmptyTable();
    }

    public void generateEmptyTable() {
        ObservableList<String> items = table.getItems();

        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 2; j++) {
                items.add("");
            }
        }
    }

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
