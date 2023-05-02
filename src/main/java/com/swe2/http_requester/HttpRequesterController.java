package com.swe2.http_requester;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Set;

import com.github.underscore.U;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.util.Callback;

/**
 * Controls the programs main view
 */
public class HttpRequesterController {
    @FXML
    private TextField urlTextField;

    @FXML
    private Label urlValidityStatus;

    @FXML
    private Button sendRequestButton;

    @FXML
    private TableView<ParametersTableContent> parametersTable;

    @FXML
    private TableColumn<ParametersTableContent, String> parameterNameColumn;

    @FXML
    private TableColumn<ParametersTableContent, String> parameterValueColumn;

    @FXML
    private ComboBox<KeyValuePair> responseKeyDropDown;

    @FXML
    private TextArea responseField;

    @FXML
    public void initialize() {
        setUrlValidtyListener();
        setUpParametersTable();
        setUpResponseDropDown();
    }

    /**
     * Sends an HTTP GET request
     * 
     * This method is called when the send button is clicked
     */
    public void sendRequest() {
        String url = urlTextField.getText();

        if (isUrlValid(url)) {
            updateParametersTable(url);

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url)).GET()
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept((response) -> {
                updateResponseField(response.body());
            });
        }
    }

    /**
     * Sets up the url validity listener
     */
    private void setUrlValidtyListener() {
        urlTextField.setOnKeyTyped(event -> {
            final String url = urlTextField.getText();

            if (url != null) {
                final boolean isUrlValid = isUrlValid(url);
                setUrlValidityStatus(isUrlValid);
            }
        });
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
     * Sets up the response key drop down
     */
    private void setUpResponseDropDown() {
        Callback<ListView<KeyValuePair>, ListCell<KeyValuePair>> factory = lv -> new ListCell<KeyValuePair>() {

            @Override
            protected void updateItem(KeyValuePair item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getKey());
            }

        };

        responseKeyDropDown.setCellFactory(factory);
        responseKeyDropDown.setButtonCell(factory.call(null));
        responseKeyDropDown.valueProperty().addListener((one, two, newItem) -> {
            responseField.setText(newItem.value);
        });
    }

    /**
     * Sets the url validity status
     * 
     * @param isValid True if the url is valid, false otherwise
     */
    private void setUrlValidityStatus(boolean isValid) {
        if (isValid) {
            urlValidityStatus.setText("OK");
            urlValidityStatus.setTextFill(Color.GREEN);
            sendRequestButton.setDisable(false);
        } else {
            urlValidityStatus.setText("NOK");
            urlValidityStatus.setTextFill(Color.RED);
            sendRequestButton.setDisable(true);

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

        if (query != null && !query.isEmpty()) {
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
     * Updates the response field
     * 
     * This method is called when the response is received. It updates the response
     * field and the response key drop down
     * with the response body.
     * If the response body is a json or xml string, it will be formatted.
     * Otherwise,
     * it will be displayed as is.
     * 
     * @param responseBody The response body from an HTTP request
     */
    private void updateResponseField(String responseBody) {
        final String prettyfiedJson = U.formatJsonOrXml(responseBody);
        responseBody = prettyfiedJson;
        updateResponseKeyDropDown(responseBody);

    }

    /**
     * Sets up the response key drop down
     * 
     * This method is called after the response body is received
     * 
     * @param responseBody The response body from an HTTP request
     */
    private void updateResponseKeyDropDown(String responseBody) {
        final String formattedResponseBody = U.formatJsonOrXml(responseBody);
        final KeyValuePair keyValuePair = new KeyValuePair("Response Body", formattedResponseBody);

        responseKeyDropDown.getItems().add(keyValuePair);

        Gson gson = new Gson();
        JsonObject jsonMap = gson.fromJson(responseBody, JsonObject.class);
        Set<Map.Entry<String, JsonElement>> mapEntries = jsonMap.entrySet();

        loopResponseMap(mapEntries);

        selectResponseBodyDropDownItem();

    }

    /**
     * Loops through a map and adds the key value pairs to the response key drop
     * down
     * 
     * @param mapEntries The map entries to loop through
     */
    private void loopResponseMap(Set<Map.Entry<String, JsonElement>> mapEntries) {
        loopResponseMap(mapEntries, "");
    }

    /**
     * Loops through a map and adds the key value pairs to the response key drop
     * down
     * 
     * @param mapEntries The map entries to loop through
     * @param keyPrefix  The key prefix to add to the key
     */
    private void loopResponseMap(Set<Map.Entry<String, JsonElement>> mapEntries, String keyPrefix) {

        mapEntries.forEach((entry) -> {
            if (entry.getValue() instanceof JsonObject) {
                JsonObject nestedMap = (JsonObject) entry.getValue();
                Set<Map.Entry<String, JsonElement>> nestedMapEntries = nestedMap.entrySet();

                loopResponseMap(nestedMapEntries, keyPrefix + entry.getKey() + ".");
            } else {
                final String mapFormattedString = U.formatJsonOrXml(entry.getValue().toString());
                final KeyValuePair keyValuePair = new KeyValuePair(keyPrefix + entry.getKey(),
                        mapFormattedString);

                responseKeyDropDown.getItems().add(keyValuePair);
            }
        });
    }

    /**
     * Selects the response body drop down item
     */
    private void selectResponseBodyDropDownItem() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                responseKeyDropDown.getSelectionModel().selectFirst();
            }
        });
    }

}
