package com.swe2.http_requester;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.github.underscore.U;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

/**
 * Controls the programs main view
 */
public class HttpRequesterController {
    @FXML
    private TextField urlTextField;

    @FXML
    private Label urlValidityStatus;

    @FXML
    private TableView<ParametersTableContent> parametersTable;

    @FXML
    private TableColumn<ParametersTableContent, String> parameterNameColumn;

    @FXML
    private TableColumn<ParametersTableContent, String> parameterValueColumn;

    @FXML
    private TextArea responseField;

    @FXML
    public void initialize() {
        setUrlValidtyListener();
        setUpParametersTable();
    }

    /**
     * Sends a http request
     * 
     * This method is called when the send button is clicked
     */
    public void sendRequest() {
        String url = urlTextField.getText();

        if (isUrlValid(url)) {
            updateParametersTable(url);

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept((response) -> {
                String prettyfiedJson = U.formatJson(response.body());
                responseField.setText(prettyfiedJson);
            });
        }

    }

    /**
     * Sets the listener for the url text field
     */
    private void setUrlValidtyListener() {
        urlTextField.setOnKeyTyped(event -> {
            final String url = urlTextField.getText();

            if (url != null && !url.isEmpty()) {
                final boolean isUrlValid = isUrlValid(url);
                setUrlValidityStatus(isUrlValid);
            }
        });

    }

    /**
     * Sets the url validity status
     * 
     * @param isValid True if the url is valid, false otherwise
     */
    private void setUrlValidityStatus(boolean isValid) {
        if (isValid) {
            urlValidityStatus.setText("Valid");
            urlValidityStatus.setTextFill(Color.GREEN);
        } else {
            urlValidityStatus.setText("Invalid");
            urlValidityStatus.setTextFill(Color.RED);
        }
    }

    /**
     * Sets up the request parameters table
     * 
     * This method is called when the view is initialized
     */
    private void setUpParametersTable() {
        parameterNameColumn.setCellValueFactory(
                new PropertyValueFactory<ParametersTableContent, String>("name"));

        parameterValueColumn.setCellValueFactory(
                new PropertyValueFactory<ParametersTableContent, String>("value"));
    }

    /**
     * Updates the request parameter list
     *
     * @param url The url to parse
     */
    private void updateParametersTable(String url) {
        parametersTable.getItems().clear();

        URI uri = URI.create(url);

        parametersTable.getItems().add(new ParametersTableContent("Scheme", uri.getScheme()));
        parametersTable.getItems().add(new ParametersTableContent("Host", uri.getHost()));
        parametersTable.getItems().add(new ParametersTableContent("Path", uri.getPath()));

        String query = uri.getQuery();

        if (!query.isEmpty()) {
            String[] parameters = query.split("&");

            for (String parameter : parameters) {
                String[] parameterParts = parameter.split("=");

                if (parameterParts.length > 1) {
                    parametersTable.getItems().add(new ParametersTableContent(parameterParts[0], parameterParts[1]));
                }
            }
        }
    }

    /**
     * Checks if an url is valid
     * 
     * @param url The url to check
     * @return True if the url is valid, false otherwise
     */
    private boolean isUrlValid(String url) {
        String regexUrlPattern = "(https?://|www\\.)[-a-zA-Z0-9+&@#/%?=~_|!:.;]*[-a-zA-Z0-9+&@#/%=~_|]";

        final boolean isUrlValid = url.matches(regexUrlPattern);

        return isUrlValid;
    }
}
