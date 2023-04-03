package com.swe2.http_requester;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HttpRequester extends Application {
    private static Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("HTTP Requester");
        /// Doesn't find fxml file on launch. No idea why
        /// For now:
        /// 1. Comment line right below
        /// 2. Launch program
        /// 3. Uncomment line right below
        /// 4. Launch program again
        scene = new Scene(loadFXML("http_requester"), 640, 800);
        stage.setScene(scene);
        stage.show();
    }

    /// Loads the UI file
    private Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
}