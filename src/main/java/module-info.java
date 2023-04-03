module com.swe2.http_requester {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.swe2.http_requester to javafx.fxml;
    exports com.swe2.http_requester;
}
