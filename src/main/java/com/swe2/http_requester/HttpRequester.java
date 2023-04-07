package com.swe2.http_requester;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main class of the HttpRequester application
 */
public class HttpRequester extends Application {
    private static Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("HTTP Requester");
        scene = new Scene(loadFXML("http_requester"), 640, 800);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Loads the fxml (UI) file
     * 
     * @param fxml The name of the fxml file
     * @return The root node of the fxml file
     * @throws IOException
     */
    private Parent loadFXML(String fxml) throws IOException {
        URL fileUrl = getClass().getResource(fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fileUrl);
        return fxmlLoader.load();
    }
}